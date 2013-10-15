/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.scala.core

import scala.language.implicitConversions
import org.vertx.java.core.AsyncResultHandler
import org.vertx.java.core.eventbus.{ Message => JMessage }
import org.vertx.scala.core.eventbus.Message
import org.vertx.java.core.impl.DefaultFutureResult
import org.vertx.scala.VertxWrapper
import scala.util.Try
import scala.util.Success
import scala.util.Failure

/**
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait FunctionConverters {

  implicit def lazyToVoidHandler(func: => Unit): Handler[Void] = new Handler[Void]() {
    override def handle(event: Void) = func
  }

  implicit def fnToHandler[T](func: T => Unit): Handler[T] = new Handler[T]() {
    override def handle(event: T) = func(event)
  }

  implicit def messageFnToJMessageHandler[T](func: Message[T] => Unit): Handler[JMessage[T]] = new Handler[JMessage[T]]() {
    override def handle(event: JMessage[T]) = func.compose(Message.apply).apply(event)
  }

  implicit def handlerToFn[T](handler: Handler[T]): T => Unit = (event: T) => handler.handle(event)

  implicit def convertFunctionToVoidAsyncHandler(func: () => Unit): AsyncResultHandler[Void] = new AsyncResultHandler[Void]() {
    override def handle(event: AsyncResult[Void]) = func()
  }

  implicit def convertFunctionToParameterisedAsyncHandler[T](func: AsyncResult[T] => Unit): AsyncResultHandler[T] = new AsyncResultHandler[T]() {
    override def handle(event: AsyncResult[T]) = func(event)
  }

  implicit def tryToAsyncResultHandler[X](tryHandler: Try[X] => Unit): AsyncResult[X] => Unit = {
    tryHandler.compose { ar: AsyncResult[X] =>
      if (ar.succeeded()) {
        Success(ar.result())
      } else {
        Failure(ar.cause())
      }
    }
  }

  def asyncResultConverter[ST, JT](mapFn: JT => ST)(handler: AsyncResult[ST] => Unit): Handler[AsyncResult[JT]] = {
    new Handler[AsyncResult[JT]]() {
      def handle(ar: AsyncResult[JT]) = {
        val scalaAr = new AsyncResult[ST]() {
          override def result(): ST = mapFn(ar.result())
          override def cause() = ar.cause()
          override def succeeded() = ar.succeeded()
          override def failed() = ar.failed()
        }
        handler(scalaAr)
      }
    }
  }

  def asyncHandler[A, B, C](handler: AsyncResult[A] => B, f: C => A): Handler[AsyncResult[C]] = {
    new Handler[AsyncResult[C]] {
      def handle(rst: AsyncResult[C]) {
        handler(
          new DefaultFutureResult[A](f(rst.result)))
      }
    }
  }

  def voidAsyncHandler(handler: AsyncResult[Unit] => Unit): Handler[AsyncResult[Void]] = {
    new Handler[AsyncResult[Void]] {
      def handle(rst: AsyncResult[Void]) {
        handler(rst.asInstanceOf[AsyncResult[Unit]])
      }
    }
  }

}

object FunctionConverters extends FunctionConverters