package org.vertx.test

import org.vertx.scala.deploy.Verticle
import org.vertx.java.core.eventbus.Message
import org.vertx.java.core.http.{ServerWebSocket => JServerWebSocket}
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.net.NetSocket
import org.vertx.scala.core.http.HttpServerRequest
import org.vertx.scala.core.http.ServerWebSocket

class SimpleVerticle extends Verticle {

  @throws(classOf[Exception])
  def start():Unit = {

    vertx
      .createNetServer
      .connectHandler({socket: NetSocket => 
        // socket
        socket.dataHandler({buffer: Buffer =>
          //
        })
        socket.drainHandler({() =>
          //
        })
        socket.endHandler({() =>
          //
        })
        socket.drainHandler({() =>
          //
        })

    }).listen(7080)

    vertx.createHttpServer.requestHandler({ req: HttpServerRequest => 
      val file : String = if (req.path == "/") "/index.html" else req.uri
      req.response.sendFile("webroot/" + file)
    }).listen(8080)

    // This looks weird, I'm probably doing something wrong.
    val closure = () => { Thread.sleep(2000L); print("hello ") }
    vertx.runOnLoop( closure )
    vertx.runOnLoop( () => { println("world") } )

    vertx.eventBus.send("test.echo", "echo!", (msg: Message[String]) => {
      printf("echo received: %s%n", msg.body)
    })

    vertx.sharedData.map("one")

    vertx
      .createHttpServer
      .websocketHandler({s: ServerWebSocket => 
        s.writeTextFrame("foo")
        s.drainHandler({() =>
          //
          })
        s.dataHandler({data: Buffer => 
          //
          })
        })
      .listen(9090)

    println("started after hello world!")
  }

  @throws(classOf[Exception])
  override def stop(): Unit = {
    super.stop
    println("stopped!")
  }
}