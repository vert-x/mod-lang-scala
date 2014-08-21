vertx.createHttpServer.requestHandler { req: HttpServerRequest =>
  req.response.end("Hello verticle script!")
}.listen(8080)
