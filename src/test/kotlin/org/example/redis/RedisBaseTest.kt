package org.example.redis

import org.example.config.BaseRedisContainer
import org.example.config.TestRedisConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestRedisConfiguration::class])
class RedisBaseTest : BaseRedisContainer(6370, "localhost") {

    @Test
    fun testGetWhenEmpty() {
        val actual = redisCommands.get("1")
        println("actual = $actual")
        assertEquals(actual, null)
    }

    @Test
    fun testGetWhenSet() {
        redisCommands.set("1", "ONE")

        val actual = redisCommands.get("1")
        println("actual = $actual")
        assertEquals(actual, "ONE")
    }
}
