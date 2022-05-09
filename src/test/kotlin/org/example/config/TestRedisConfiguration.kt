package org.example.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@TestConfiguration
class TestRedisConfiguration(
    @Value("\${spring.redis.port}") val redisPort: Int,
    @Value("\${spring.redis.host}") val redisHost: String
) {

    var redisServer: RedisServer = RedisServer(redisPort)

    @PostConstruct
    fun postConstruct() {
        redisServer.start()
    }

    @PreDestroy
    fun preDestroy() {
        redisServer.stop()
    }
}
