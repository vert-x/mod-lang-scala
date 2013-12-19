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
package org.vertx.scala.testtools

import org.vertx.testtools.JavaClassRunner
import org.junit.runners.model.FrameworkMethod

class ScalaClassRunner(klass: Class[_]) extends JavaClassRunner(klass) {

  override protected def computeTestMethods(): java.util.List[FrameworkMethod] = {
    val testClass = getTestClass.getJavaClass
    if (!classOf[TestVerticle].isAssignableFrom(testClass)) {
      throw new IllegalArgumentException("Scala Test classes must extend org.vertx.scala.testtools.TestVerticle")
    }
    this.main = "scala:" + testClass.getName
    getTestMethods
  }

}