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

import org.vertx.java.core.dns.{ SrvRecord => JSrvRecord }

/**
 * Represent a Service-Record (SRV) which was resolved for a domain.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
final class SrvRecord private[scala] (val asJava: JSrvRecord) extends AnyVal {

  /**
   * Returns the priority for this service record.
   */
  def priority(): Int = asJava.priority()

  /**
   * Returns the weight of this service record.
   */
  def weight(): Int = asJava.weight()

  /**
   * Returns the port the service is running on.
   */
  def port(): Int = asJava.port()

  /**
   * Returns the name for the server being queried.
   */
  def name(): String = asJava.name()

  /**
   * Returns the protocol for the service being queried (i.e. "_tcp").
   */
  def protocol(): String = asJava.protocol()

  /**
   * Returns the service's name (i.e. "_http").
   */
  def service(): String = asJava.service()

  /**
   * Returns the name of the host for the service.
   */
  def target(): String = asJava.target()
}

/** Factory for [[org.vertx.scala.core.dns.SrvRecord]] instances. */
object SrvRecord {
  def apply(internal: JSrvRecord) = new SrvRecord(internal)
}
