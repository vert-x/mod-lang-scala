/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.scala.core.logging

import org.vertx.java.core.logging.{ Logger => JLogger }

/**
 * Small helper class to check for log level and delegate it to the real logger if enabled.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
// constructor is private because users should use apply in companion
// extends AnyVal to avoid object allocation and improve performance
final class Logger private[scala] (val asJava: JLogger) extends AnyVal {

  def isInfoEnabled(): Boolean = asJava.isInfoEnabled()
  def isDebugEnabled(): Boolean = asJava.isDebugEnabled()
  def isTraceEnabled(): Boolean = asJava.isTraceEnabled()

  def trace(message: => AnyRef) = withTrace(asJava.trace(message))
  def trace(message: => AnyRef, t: => Throwable) = withTrace(asJava.trace(message, t))
  def debug(message: => AnyRef) = withDebug(asJava.debug(message))
  def debug(message: => AnyRef, t: => Throwable) = withDebug(asJava.debug(message, t))
  def info(message: => AnyRef) = withInfo(asJava.info(message))
  def info(message: => AnyRef, t: => Throwable) = withInfo(asJava.info(message, t))

  def warn(message: => AnyRef) = asJava.warn(message)
  def warn(message: => AnyRef, t: => Throwable) = asJava.warn(message, t)
  def error(message: => AnyRef) = asJava.error(message)
  def error(message: => AnyRef, t: => Throwable) = asJava.error(message, t)
  def fatal(message: => AnyRef) = asJava.fatal(message)
  def fatal(message: => AnyRef, t: => Throwable) = asJava.fatal(message, t)

  private def withTrace(fn: => Unit) = if (asJava.isTraceEnabled) { fn }
  private def withDebug(fn: => Unit) = if (asJava.isDebugEnabled) { fn }
  private def withInfo(fn: => Unit) = if (asJava.isInfoEnabled) { fn }

}

/** Factory for [[org.vertx.scala.core.logging.Logger]] instances. */
object Logger {
  def apply(internal: JLogger): Logger = new Logger(internal)
}