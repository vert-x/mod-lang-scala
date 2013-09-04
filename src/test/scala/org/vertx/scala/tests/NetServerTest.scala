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

package org.vertx.scala.tests

import org.vertx.testtools.TestVerticle
import org.vertx.testtools.VertxAssert.testComplete
import org.vertx.testtools.VertxAssert.assertEquals
import org.junit.Test
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.testframework.TestUtils
import java.nio.file.FileSystems

/**
 * @author Edgar Chan
 */
class NetServerTest extends TestVerticle {

  import org.vertx.scala.core._
  import org.vertx.scala.core.buffer.Buffer._

  lazy val sVertx = Vertx(getVertx)

  @Test
  def echoNoSSLTest() {
    echoTest(withSSl = false)
  }

  @Test
  def echoSSLTest() {
    echoTest(withSSl = true)
  }

  private def echoTest(withSSl: Boolean) {
    // FIXME test
    testComplete()
    //    val server = sVertx.createNetServer
    //    val client = sVertx.createNetClient
    //
    //
    //    if (withSSl){
    //      server.setSSL(true)
    //      .setKeyStorePath(testResourcesPath("server-keystore.jks"))
    //      .setKeyStorePassword("wibble")
    //      .setTrustStorePath(testResourcesPath("server-truststore.jks"))
    //      .setTrustStorePassword("wibble")
    //      .setClientAuthRequired(true)
    //
    //      client.setSSL(true)
    //      .setKeyStorePath(testResourcesPath("client-keystore.jks"))
    //      .setKeyStorePassword("wibble")
    //      .setTrustStorePath(testResourcesPath("client-truststore.jks"))
    //      .setTrustStorePassword("wibble")
    //    }
    //
    //
    //    server.connectHandler( socket =>
    //      socket.dataHandler{
    //        buff => socket.write(buff)
    //      }
    //    ).listen(8080, async =>{
    //
    //      assertEquals(true, async.succeeded)
    //      assertEquals(server.internal, async.result.internal)
    //
    //      client.connect(8080, "localhost", async1 =>{
    //        assertEquals(true, async1.succeeded)
    //
    //        val socket = async1.result
    //        val sends = 10
    //        val size = 100
    //
    //        val sent = new Buffer()
    //
    //        val received = new Buffer()
    //
    //        socket.dataHandler( buffer =>{
    //          received.append(buffer)
    //
    //          if(received.length == sends * size){
    //            TestUtils.bufferEquals(sent, received)
    //            server.close(() =>{
    //                client.close
    //                testComplete()
    //            })
    //          }
    //        })
    //
    //        socket.pause()
    //
    //        socket.resume()
    //
    //        (1 to sends).foreach{ s =>
    //          val data = TestUtils.generateRandomBuffer(size)
    //          sent.append(data)
    //          socket.write(data)
    //        }
    //
    //      })
    //    })
  }

  private def testResourcesPath(fn: String) = {
    val path = FileSystems.getDefault.getPath(this.getClass.getClassLoader.getResource("").getPath)
    val full = List(path.getParent.getParent.toAbsolutePath, "resources", "test", "keystores").mkString("/")
    s"""$full/$fn"""
  }

}