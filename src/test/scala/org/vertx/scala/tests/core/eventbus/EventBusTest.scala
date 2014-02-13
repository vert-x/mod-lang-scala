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
package org.vertx.scala.tests.core.eventbus

import org.junit.Test
import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.core.eventbus.{RegisteredHandler, Message}
import org.vertx.scala.core.json.Json
import java.util.concurrent.atomic.AtomicInteger
import org.vertx.scala.core.json.JsonObject
import org.vertx.scala.core.AsyncResult

class EventBusTest extends TestVerticle {

  @Test
  def registerHandler(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      assertThread()
      assertEquals("hello", msg.address())
      testComplete
    })

    vertx.eventBus.send("hello", true)
  }

  @Test
  def replyBack(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      assertThread()
      msg.reply("hello " + msg.body)
    })

    vertx.eventBus.send("hello", "world", { reply: Message[String] =>
      assertThread()
      assertEquals("hello world", reply.body)
      testComplete
    })
  }

  @Test
  def registerMultipleHandlers(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      assertThread()
      msg.reply("hello " + msg.body)
    })
    vertx.eventBus.registerHandler("goodbye", { msg: Message[String] =>
      assertThread()
      msg.reply("goodbye " + msg.body)
    })

    vertx.eventBus.send("hello", "world", { reply: Message[String] =>
      assertThread()
      assertEquals("hello world", reply.body)
      vertx.eventBus.send("goodbye", "abc", { reply: Message[String] =>
        assertEquals("goodbye abc", reply.body)
        testComplete
      })
    })
  }

  @Test
  def publishing(): Unit = {
    val waitingFor = new AtomicInteger(2)
    vertx.eventBus.registerHandler("listeners", { msg: Message[String] =>
      assertThread()
      assertEquals("yo!", msg.body)
      if (waitingFor.decrementAndGet == 0) {
        testComplete
      }
    })
    vertx.eventBus.registerHandler("listeners", { msg: Message[String] =>
      assertThread()
      assertEquals("yo!", msg.body)
      if (waitingFor.decrementAndGet == 0) {
        testComplete
      }
    })

    vertx.eventBus.publish("listeners", "yo!")
  }

  @Test
  def onlyOneReceiverForSend(): Unit = {
    val waitingFor = new AtomicInteger(0)
    vertx.eventBus.registerHandler("listeners", { msg: Message[String] =>
      assertThread()
      assertEquals("yo!", msg.body)
      waitingFor.incrementAndGet()
    })
    vertx.eventBus.registerHandler("listeners", { msg: Message[String] =>
      assertThread()
      assertEquals("yo!", msg.body)
      waitingFor.incrementAndGet()
    })

    vertx.eventBus.send("listeners", "yo!")

    vertx.setTimer(500, { timerId =>
      assertThread()
      assertEquals(1, waitingFor.get())
      testComplete()
    })
  }

  @Test
  def replyBackTwice(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      assertThread()
      val who = msg.body
      msg.reply("hello " + who, { followUp: Message[String] =>
        assertEquals("me again!", followUp.body)
        followUp.reply("hello again, " + who)
      })
    })

    vertx.eventBus.send("hello", "world", { reply: Message[String] =>
      assertThread()
      assertEquals("hello world", reply.body)
      reply.reply("me again!", { reply2: Message[String] =>
        assertEquals("hello again, world", reply2.body)
        testComplete
      })
    })
  }

  @Test
  def replyBackWithMultipleClients(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      assertThread()
      val who = msg.body
      msg.reply("hello " + who, { followUp: Message[String] =>
        assertEquals("me again!", followUp.body)
        followUp.reply("hello again, " + who)
      })
    })

    vertx.eventBus.send("hello", "world", { reply: Message[String] =>
      assertThread()
      assertEquals("hello world", reply.body)
      vertx.eventBus.send("hello", "other", { otherReply: Message[String] =>
        assertThread()
        assertEquals("hello other", otherReply.body)
        otherReply.reply("me again!", { otherReply2: Message[String] =>
          assertThread()
          assertEquals("hello again, other", otherReply2.body)
          reply.reply("me again!", { reply2: Message[String] =>
            assertThread()
            assertEquals("hello again, world", reply2.body)
            testComplete
          })
        })
      })
    })
  }

  @Test
  def sendingDifferentTypes(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      val who = msg.body
      msg.reply(who.hashCode(), { followUp: Message[Integer] =>
        assertThread()
        assertEquals(42, followUp.body)
        followUp.reply(Json.obj("message" -> ("hello again, " + who), "someBool" -> true))
      })
    })

    vertx.eventBus.send("hello", "world", { reply: Message[String] =>
      assertThread()
      assertEquals("world".hashCode(), reply.body)
      reply.reply(42, { reply2: Message[JsonObject] =>
        assertEquals(Json.obj("message" -> "hello again, world", "someBool" -> true), reply2.body)
        testComplete
      })
    })
  }

  @Test
  def unregisteringHandler(): Unit = {
    var rh: RegisteredHandler[String] = null

    def unregister() {
      vertx.setTimer(200, { timerId =>
        assertThread()
        rh.unregister({ ar =>
          assertThread()
          if (ar.succeeded()) {
            vertx.eventBus.sendWithTimeout("hello", "test", 500, { ar2: AsyncResult[_] =>
              if (ar2.succeeded()) {
                fail("Should not be able to send a message to unregistered handler")
              } else {
                testComplete()
              }
            })
          } else {
            fail("Should be able to unregister handler, but got " + ar.cause().getMessage())
          }
        })
      })
    }

    rh = vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      assertThread()
      fail("should not get here!")
    }, { ar =>
      if (ar.succeeded()) {
        unregister()
      } else {
        fail("Should be able to register handler")
      }
    })
  }
}