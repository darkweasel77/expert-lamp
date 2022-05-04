package org.example.service

import io.mockk.*
import org.example.model.NotFoundException
import org.example.model.InterestingObject
import org.example.redis.RedisService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalCacheUnitTest {

    private val redisService = mockk<RedisService>()

    private val cache = LocalCache(10L, 2000, 10000, redisService)

    @BeforeEach
    fun setup() {
        clearAllMocks()
        justRun { redisService.write(any()) }
    }

    @Test
    fun `get value from cache, loads cache and doesn't hit redis again`() {
        every { redisService.read(any()) } returns InterestingObject(1, "ONE")

        val actual = cache.getValueForKey(1)
        assertEquals(actual.value, "ONE")
        verify(exactly = 1) { redisService.read(any()) }

        // call again, shouldn't call back to redis
        val actual2 = cache.getValueForKey(1)
        assertEquals(actual2.value, "ONE")
        verify(exactly = 1) { redisService.read(any()) }
    }

    @Test
    fun `get value from cache when present but hits redis when lru expired`() {
        every { redisService.read(any()) } returns InterestingObject(1, "ONE")

        val actual = cache.getValueForKey(1)
        assertEquals(actual.value, "ONE")
        verify(exactly = 1) { redisService.read(any()) }

        Thread.sleep(3000)
        val actual2 = cache.getValueForKey(1)
        assertEquals(actual2.value, "ONE")
        verify(exactly = 2) { redisService.read(any()) }
    }

    @Test
    fun `get value from cache when present but hits redis when global expired`() {
        every { redisService.read(any()) } returns InterestingObject(1, "ONE")

        for (i in 1..10) {
            val actual = cache.getValueForKey(1)
            assertEquals(actual.value, "ONE")
            verify(exactly = 1) { redisService.read(any()) }
            Thread.sleep(1000)
        }

        Thread.sleep(1000)
        val actual2 = cache.getValueForKey(1)
        assertEquals(actual2.value, "ONE")
        verify(exactly = 2) { redisService.read(any()) }
    }

    @Test
    fun `cache size drops lru when over size`() {
        // fill up cache
        for (i in 1..10) {
            every { redisService.read(i) } returns InterestingObject(1, "$i")
            val actual = cache.getValueForKey(i)
            assertEquals(actual.value, "$i")
            verify(exactly = 1) { redisService.read(i) }
        }
        // redis shouldn't be called
        for (i in 1..10) {
            val actual = cache.getValueForKey(i)
            assertEquals(actual.value, "$i")
            verify(exactly = 1) { redisService.read(i) }
        }

        // add another, lru should drop
        every { redisService.read(11) } returns InterestingObject(1, "11")
        val actual = cache.getValueForKey(11)
        val actual2 = cache.getValueForKey(11)
        assertEquals(actual.value, "11")
        assertEquals(actual2.value, "11")
        verify(exactly = 1) { redisService.read(11) }

        // check 'first' called 1
        val actualOne = cache.getValueForKey(1)
        assertEquals(actualOne.value, "1")
        verify(exactly = 2) { redisService.read(1) }
    }

    @Test
    fun `handles safely when redis does not have value`() {
        every { redisService.read(any()) } throws NotFoundException("oops", Exception())
        assertThrows(NotFoundException::class.java) {
            cache.getValueForKey(1)
        }
    }
}
