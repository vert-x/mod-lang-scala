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

trait ClientConfigurer {

  def close():Unit

  def bossThreads():Int

  def connectTimeout():Long

  def keyStorePassword():String

  def keyStorePassword(keyStorePassword: String):ClientConfigurer.this.type

  def keyStorePath():String

  def keyStorePath(keyStorePath: String):ClientConfigurer.this.type

  def receiveBufferSize():Int

  def receiveBufferSize(receiveBufferSize: Int):ClientConfigurer.this.type

  def sendBufferSize():Int

  def sendBufferSize(sendBufferSize: Int):ClientConfigurer.this.type

  def trafficClass():Int

  def trafficClass(trafficClass: Int):ClientConfigurer.this.type

  def trustStorePassword():String

  def trustStorePassword(password: String):ClientConfigurer.this.type

  def trustStorePath():String

  def trustStorePath(path: String):ClientConfigurer.this.type

}