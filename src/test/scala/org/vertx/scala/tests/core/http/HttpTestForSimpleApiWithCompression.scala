package org.vertx.scala.tests.core.http

import org.junit.Test

class HttpTestForSimpleApiWithCompression extends HttpTestForSimpleApi {
  override protected def compression = true
  @Test override def createHttpServer(): Unit = super.createHttpServer()
  @Test override def httpsServer(): Unit = super.httpsServer()
  @Test override def invalidPort(): Unit = super.invalidPort()
  @Test override def invalidHost(): Unit = super.invalidHost()
  @Test override def postMethod(): Unit = super.postMethod()
  @Test override def getMethod(): Unit = super.getMethod()
  @Test override def headMethod(): Unit = super.headMethod()
  @Test override def connectMethod(): Unit = super.connectMethod()
  @Test override def getRequestMethod(): Unit = super.getRequestMethod()
  @Test override def postRequestMethod(): Unit = super.postRequestMethod()
  @Test override def putRequestMethod(): Unit = super.putRequestMethod()
  @Test override def deleteRequestMethod(): Unit = super.deleteRequestMethod()
  @Test override def headRequestMethod(): Unit = super.headRequestMethod()
  @Test override def traceRequestMethod(): Unit = super.traceRequestMethod()
  @Test override def connectRequestMethod(): Unit = super.connectRequestMethod()
  @Test override def optionsRequestMethod(): Unit = super.optionsRequestMethod()
  @Test override def patchRequestMethod(): Unit = super.patchRequestMethod()
}