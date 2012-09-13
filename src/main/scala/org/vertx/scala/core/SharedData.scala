package org.vertx.scala.core

import scala.collection.JavaConversions._
import scala.collection.mutable.ConcurrentMap
import scala.collection.mutable.Set

class SharedData(internal: org.vertx.java.core.shareddata.SharedData) {

  def map[K,V](name: String):ConcurrentMap[K,V] = {
    val map: java.util.concurrent.ConcurrentMap[K,V] = internal.getMap(name)
    map
  }

  def set[T](name: String):Set[T] = {
    val set: java.util.Set[T] = internal.getSet(name)
    set
  }

  def removeMap[T](name: Any):Boolean = {
    internal.removeMap(name)
  }

  def removeSet[T](name: String):Boolean = {
    internal.removeSet(name)
  }

}