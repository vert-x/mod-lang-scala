/*
 * Copyright 2011-2013 the original author or authors.
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

package org.vertx.scala.net

import scala.language.implicitConversions
import org.vertx.java.core.net.{NetSocket => JNetSocket}
import org.vertx.java.core.Handler
import org.vertx.java.core.net.{NetSocket => JNetSocket}

object ConnectHandler1 {
  def apply(socket: (NetSocket) => Unit) =
    new ConnectHandler1(socket)
}

class ConnectHandler1(delegate: (NetSocket) => Unit) extends Handler[JNetSocket] {

  implicit def convertJavaToScalaNetSocket(jsocket: JNetSocket):NetSocket = {
    NetSocket(jsocket)
  }

  def handle(jsocket: JNetSocket) {
    delegate(jsocket)
  }

}