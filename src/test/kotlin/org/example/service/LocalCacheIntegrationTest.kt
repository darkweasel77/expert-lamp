package org.example.service

import org.example.config.BaseRedisContainer
import org.example.config.TestRedisConfiguration
import org.example.model.NotFoundException
import org.example.model.InterestingObject
import org.example.redis.RedisService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestRedisConfiguration::class])
class LocalCacheIntegrationTest : BaseRedisContainer(6370, "localhost") {
    @Autowired
    lateinit var redisService: RedisService

    @Autowired
    lateinit var cache: LocalCache

    @Test
    fun `get value from cache when present`() {
        redisService.write(InterestingObject(1, "ONE"))

        val actual = cache.getValueForKey(1)
        assertEquals(actual.value, "ONE")
    }

    @Test
    fun `get value from cache when present but lru expired`() {
        redisService.write(InterestingObject(1, "ONE"))

        val actual = cache.getValueForKey(1)
        assertEquals(actual.value, "ONE")

        Thread.sleep(3000)
        val actual2 = cache.getValueForKey(1)
        assertEquals(actual2.value, "ONE")
    }

    @Test
    fun `get value from cache when present but global expired`() {
        redisService.write(InterestingObject(1, "ONE"))

        val actual = cache.getValueForKey(1)
        assertEquals(actual.value, "ONE")

        Thread.sleep(11000)
        val actual2 = cache.getValueForKey(1)
        assertEquals(actual2.value, "ONE")
    }

    @Test
    fun `handles safely when redis does not have value`() {
        assertThrows(NotFoundException::class.java) {
            cache.getValueForKey(2)
        }
    }
}
