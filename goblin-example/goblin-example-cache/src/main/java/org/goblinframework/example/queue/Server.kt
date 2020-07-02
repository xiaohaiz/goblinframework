package org.goblinframework.example.queue

import org.goblinframework.bootstrap.core.StandaloneServer
import org.goblinframework.cache.redis.client.RedisClientManager
import org.goblinframework.core.container.GoblinSpringContainer
import org.goblinframework.core.container.SpringContainer

@GoblinSpringContainer("/config/cache.xml")
class Server : StandaloneServer() {
  override fun doService(container: SpringContainer?) {
    val redisClient = RedisClientManager.INSTANCE.getRedisClient("example")
    val redisHashCommands = redisClient!!.getRedisCommands().sync().redisHashCommands
    redisHashCommands.hset("test", "id", "123456")
    redisHashCommands.hset("test", "name", "654321")
    println("test.id [${redisHashCommands.hget("test", "id")}")
    println("test.name [${redisHashCommands.hget("test", "name")}")
  }
}

fun main(args: Array<String>) {
  Server().bootstrap(args)
}