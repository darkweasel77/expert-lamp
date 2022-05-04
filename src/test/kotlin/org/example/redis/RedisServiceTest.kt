package org.example.redis

import org.example.config.BaseRedisContainer
import org.example.config.TestRedisConfiguration
import org.example.model.NotFoundException
import org.example.model.InterestingObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestRedisConfiguration::class])
class RedisServiceTest : BaseRedisContainer(6370, "localhost") {

    @Autowired
    lateinit var redisService: RedisService

    @Test
    fun testGetWhenEmpty() {
        assertThrows(NotFoundException::class.java) {
            redisService.read(1)
        }
    }

    @Test
    fun testGetWhenSet() {
        redisService.write(InterestingObject(1, "ONE"))

        val actual = redisService.read(1)
        println("actual = $actual")
        assertEquals(actual.value, "ONE")
    }
}
