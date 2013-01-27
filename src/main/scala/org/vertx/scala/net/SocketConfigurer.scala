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

trait SocketConfigurer {

  def listen(port: Int):SocketConfigurer.this.type

  def listen(port: Int, address: String):SocketConfigurer.this.type

  def acceptBacklog():Int

  def acceptBacklog(backlog: Int):SocketConfigurer.this.type

  def keyStorePassword():String

  def keyStorePassword(keyStorePassword: String):SocketConfigurer.this.type

  def keyStorePath():String

  def keyStorePath(keyStorePath: String):SocketConfigurer.this.type

  def receiveBufferSize():Int

  def receiveBufferSize(receiveBufferSize: Int):SocketConfigurer.this.type

  def sendBufferSize():Int

  def sendBufferSize(sendBufferSize: Int):SocketConfigurer.this.type

  def trafficClass():Int

  def trafficClass(trafficClass: Int):SocketConfigurer.this.type

  def trustStorePassword():String

  def trustStorePassword(password: String):SocketConfigurer.this.type

  def trustStorePath():String

  def trustStorePath(path: String):SocketConfigurer.this.type

}