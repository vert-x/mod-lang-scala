package org.vertx.scala.tests.core.eventbus

import org.junit.Test
import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.core.eventbus.Message
import org.vertx.scala.core.json.Json
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger
import org.vertx.scala.core.json.JsonObject
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.FunctionConverters._
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import org.vertx.java.core.eventbus.ReplyException

class EventBusTest extends TestVerticle {

  @Test
  def registerHandler(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[_] =>
      testComplete
    })

    vertx.eventBus.send("hello", true)
  }

  @Test
  def replyBack(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      msg.reply("hello " + msg.body)
    })

    vertx.eventBus.send("hello", "world", { reply: Message[String] =>
      assertEquals("hello world", reply.body)
      testComplete
    })
  }

  @Test
  def registerMultipleHandlers(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      msg.reply("hello " + msg.body)
    })
    vertx.eventBus.registerHandler("goodbye", { msg: Message[String] =>
      msg.reply("goodbye " + msg.body)
    })

    vertx.eventBus.send("hello", "world", { reply: Message[String] =>
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
      assertEquals("yo!", msg.body)
      if (waitingFor.decrementAndGet == 0) {
        testComplete
      }
    })
    vertx.eventBus.registerHandler("listeners", { msg: Message[String] =>
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
      assertEquals("yo!", msg.body)
      waitingFor.incrementAndGet()
    })
    vertx.eventBus.registerHandler("listeners", { msg: Message[String] =>
      assertEquals("yo!", msg.body)
      waitingFor.incrementAndGet()
    })

    vertx.eventBus.send("listeners", "yo!")

    vertx.setTimer(500, { timerId =>
      assertEquals(1, waitingFor.get())
      testComplete()
    })
  }

  @Test
  def replyBackTwice(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      val who = msg.body
      msg.reply("hello " + who, { followUp: Message[String] =>
        assertEquals("me again!", followUp.body)
        followUp.reply("hello again, " + who)
      })
    })

    vertx.eventBus.send("hello", "world", { reply: Message[String] =>
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
      val who = msg.body
      msg.reply("hello " + who, { followUp: Message[String] =>
        assertEquals("me again!", followUp.body)
        followUp.reply("hello again, " + who)
      })
    })

    vertx.eventBus.send("hello", "world", { reply: Message[String] =>
      assertEquals("hello world", reply.body)
      vertx.eventBus.send("hello", "other", { otherReply: Message[String] =>
        assertEquals("hello other", otherReply.body)
        otherReply.reply("me again!", { otherReply2: Message[String] =>
          assertEquals("hello again, other", otherReply2.body)
          reply.reply("me again!", { reply2: Message[String] =>
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
        assertEquals(42, followUp.body)
        followUp.reply(Json.obj("message" -> ("hello again, " + who), "someBool" -> true))
      })
    })

    vertx.eventBus.send("hello", "world", { reply: Message[String] =>
      assertEquals("world".hashCode(), reply.body)
      reply.reply(42, { reply2: Message[JsonObject] =>
        assertEquals(Json.obj("message" -> "hello again, world", "someBool" -> true), reply2.body)
        testComplete
      })
    })
  }

}