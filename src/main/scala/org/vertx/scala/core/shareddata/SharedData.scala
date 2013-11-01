package org.vertx.scala.core.shareddata

import org.vertx.java.core.shareddata.{ SharedData => JSharedData }
import scala.collection.JavaConversions._
import scala.collection.concurrent.Map
import scala.collection.mutable.Set
import org.vertx.scala.VertxWrapper

class SharedData(protected val internal: JSharedData) extends VertxWrapper {
  override type InternalType = JSharedData

  /**
   * Return a {@code Map} with the specific {@code name}. All invocations of this method with the same value of {@code name}
   * are guaranteed to return the same {@code Map} instance. <p>
   */
  def getMap[K, V](name: String): Map[K, V] = mapAsScalaConcurrentMap(internal.getMap(name))

  /**
   * Return a {@code Set} with the specific {@code name}. All invocations of this method with the same value of {@code name}
   * are guaranteed to return the same {@code Set} instance. <p>
   */
  def getSet[E](name: String): Set[E] = asScalaSet(internal.getSet(name))

  /**
   * Remove the {@code Map} with the specific {@code name}.
   */
  def removeMap(name: String): Boolean = internal.removeMap(name)

  /**
   * Remove the {@code Set} with the specific {@code name}.
   */
  def removeSet(name: String): Boolean = internal.removeSet(name)

}

object SharedData {
  def apply(actual: JSharedData) = new SharedData(actual)
}
