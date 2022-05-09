package org.example.config

import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestRedisConfiguration::class])
abstract class BaseRedisContainer() {
    abstract val redisPort: Int
    abstract val redisHost: String

    lateinit var redisClient: RedisClient
    lateinit var redisCommands: RedisCommands<String, String>

    @BeforeEach
    open fun setupRedisClient() {
        redisClient = RedisClient.create("redis://$redisHost:$redisPort")
        redisCommands = redisClient.connect().sync()
    }

    @AfterEach
    open fun removeAllDataFromRedis() {
        redisClient.connect().sync().flushall()
    }
}
