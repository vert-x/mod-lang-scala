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
package org.vertx.scala.core.dns

import org.vertx.scala.core._
import org.vertx.java.core.dns.{ DnsException => JDnsException }

/**
 * [[org.vertx.scala.core.AsyncResult]] wrapping failured conditions as a
 * result of [[org.vertx.scala.core.dns.DnsException]]
 *
 * @author Galder Zamarreño
 */
class DnsExceptionAsyncResult[R](val t: JDnsException, val ar: AsyncResult[R]) extends AsyncResult[R] {
  def cause(): Throwable = DnsException(DnsResponseCode.fromJava(t.code()))
  def failed(): Boolean = true
  def result(): R = ar.result()
  def succeeded(): Boolean = false
}
