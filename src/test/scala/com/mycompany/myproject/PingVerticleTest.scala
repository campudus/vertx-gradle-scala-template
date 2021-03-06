package com.mycompany.myproject

import org.junit.Test
import scala.concurrent.Promise
import scala.concurrent.Future
import scala.util.{Try, Failure, Success}
import org.vertx.scala.core.json.Json
import org.vertx.scala.testtools.TestVerticle
import org.vertx.scala.core.eventbus.Message
import org.vertx.scala.core.FunctionConverters._
import org.vertx.testtools.VertxAssert._

class PingVerticleTest extends TestVerticle {

  override def asyncBefore(): Future[Unit] = {
    val p = Promise[Unit]
    container.deployModule("com.mycompany~my-module~0.1.0-SNAPSHOT", Json.obj(), 1, {
      case Success(deploymentId) => p.success()
      case Failure(ex) => p.failure(ex)
    }: Try[String] => Unit)
    p.future
  }

  @Test
  def pingPongTest() {
    vertx.eventBus.send("ping", "ping", { reply: Message[String] =>
      assertEquals("pong", reply.body())
      testComplete()
    })
  }

}
