package org.vertx.scala.tests.core.eventbus

import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert._
import scala.util.Failure
import scala.util.Try
import org.vertx.scala.core.eventbus.Message
import org.vertx.scala.core.FunctionConverters._
import org.junit.Test
import scala.util.Success
import org.vertx.java.core.eventbus.ReplyException
import org.junit.Ignore
import org.vertx.scala.core.AsyncResult
import org.vertx.java.core.eventbus.ReplyFailure

class EventBusTimeoutsTest extends TestVerticle {
  @Test
  def replyWithinTimeout(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      vertx.setTimer(100, timer => msg.reply("hello " + msg.body))
    })

    vertx.eventBus.sendWithTimeout("hello", "reply-within-timeout", 500, {
      case Success(reply) =>
        assertEquals("hello reply-within-timeout", reply.body)
        testComplete
      case Failure(ex) => fail("Should receive message within time, but got exception: " + ex)
    }: Try[Message[String]] => Unit)
  }

  @Test
  def noReplyWithinTimeout(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      // do not reply at all
    })

    vertx.eventBus.sendWithTimeout("hello", "no-reply-within-timeout", 100, {
      case Success(result) => fail("Should not receive a message within time, but got one: " + result.body)
      case Failure(ex) => testComplete
    }: Try[Message[String]] => Unit)
  }

  @Test
  def noReplyWithinTimeoutNoRegisteredHandler(): Unit = {
    vertx.eventBus.sendWithTimeout("hello", "missing-handler", 100, {
      case Success(result) => fail("Should not receive a message within time, but got one: " + result.body)
      case Failure(ex) => testComplete
    }: Try[Message[String]] => Unit)
  }

  @Test
  def lateReplyAfterTimeout(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      vertx.setTimer(300, timer => msg.reply("hello " + msg.body))
    })

    vertx.eventBus.sendWithTimeout("hello", "late-reply-within-timeout", 100, {
      case Success(result) => fail("Should not receive a message within time, but got one: " + result.body)
      case Failure(ex) =>
        assertTrue("exception should be of kind ReplyException", ex.isInstanceOf[ReplyException])
        assertEquals("exception should be a TIMEOUT", "TIMEOUT", ex.asInstanceOf[ReplyException].failureType().name())
        // Wait to complete test, so if a message still comes, it shouldn't be received here anymore
        vertx.setTimer(300, timer => testComplete)
    }: Try[Message[String]] => Unit)
  }

  @Test
  def replyReplyWithinTimeout(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      msg.replyWithTimeout("hello " + msg.body, 500, {
        case Success(reply) =>
          assertEquals("got it", reply.body)
          testComplete
        case Failure(ex) => fail("Should get a reply on reply in time, but got " + ex.getMessage)
      }: Try[Message[String]] => Unit)
    })

    vertx.eventBus.sendWithTimeout("hello", "reply-within-timeout", 250, {
      case Success(reply) =>
        assertEquals("hello reply-within-timeout", reply.body)
        reply.reply("got it")
      case Failure(ex) =>
        fail("Should receive message within time, but got exception in first send: " + ex)
    }: Try[Message[String]] => Unit)
  }

  @Test
  def replyNoReplyWithinTimeout(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      msg.replyWithTimeout("hello " + msg.body, 100, {
        case Success(reply) => fail("Should not get a reply in time, but got " + reply.body)
        case Failure(ex) => testComplete
      }: Try[Message[String]] => Unit)
    })

    vertx.eventBus.sendWithTimeout("hello", "no-reply-within-timeout", 200, {
      case Success(msg) => vertx.setTimer(500, { timer => msg.reply("replying late") })
      case Failure(ex) => fail("Should receive a message within time at first, but got: " + ex.getMessage())
    }: Try[Message[String]] => Unit)
  }

  @Test
  def replyNoReplyWithinTimeoutNoRegisteredHandler(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      logger.info("received first message")
      assertEquals("Should receive a first message before sending reply", "missing-handler", msg.body)
      msg.replyWithTimeout("hello " + msg.body, 200, {
        case Success(reply) => fail("Should not get a reply in time, but got " + reply.body)
        case Failure(ex) =>
          assertTrue("exception should be of kind ReplyException", ex.isInstanceOf[ReplyException])
          assertEquals("exception should be a NO_HANDLERS", "NO_HANDLERS", ex.asInstanceOf[ReplyException].failureType().name())
          testComplete
      }: Try[Message[String]] => Unit)
      logger.info("replied to message with 'hello " + msg.body + "'")
    })

    vertx.eventBus.send("hello", "missing-handler")
  }

  @Test
  def replyLateReplyAfterTimeout(): Unit = {
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      assertEquals("late-reply-within-timeout", msg.body)
      msg.replyWithTimeout("late-reply", 100, {
        case Success(result) => fail("Should not receive a message within time, but got one: " + result.body)
        case Failure(ex) =>
          assertTrue("exception should be of kind ReplyException", ex.isInstanceOf[ReplyException])
          assertEquals("exception should be a TIMEOUT", "TIMEOUT", ex.asInstanceOf[ReplyException].failureType().name())
          // Wait to complete test, so if a message still comes, it shouldn't be received here anymore
          vertx.setTimer(300, timer => testComplete)
      }: Try[Message[String]] => Unit)
    })

    vertx.eventBus.send("hello", "late-reply-within-timeout", { msg: Message[String] =>
      assertEquals("Should get a quick reply before late reply", "late-reply", msg.body)
      vertx.setTimer(300, timer => msg.reply("hello " + msg.body))
    })
  }

  @Test
  def replyWithFailure(): Unit = {
    val failText = "there's something strange... in the neighborhood."
    vertx.eventBus.registerHandler("hello", { msg: Message[String] =>
      msg.fail(123, failText)
    })

    vertx.eventBus.sendWithTimeout("hello", "who you gonna call?", 200, { ar: AsyncResult[Message[String]] =>
      if (ar.succeeded()) {
        fail("Should not succeed!")
      } else {
        assertTrue("Should be a ReplyException", ar.cause().isInstanceOf[ReplyException])
        val ex = ar.cause().asInstanceOf[ReplyException]
        assertEquals("Should get 123 as failureCode", 123, ex.failureCode())
        assertEquals("Should get RECIPIENT_FAILURE as failureType", ReplyFailure.RECIPIENT_FAILURE, ex.failureType())
        assertEquals("Should get " + failText + " as message", failText, ex.getMessage())
        testComplete()
      }
    })
  }
}