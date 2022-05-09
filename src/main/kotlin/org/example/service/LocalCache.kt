package org.example.service

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import mu.KotlinLogging
import org.example.model.NotFoundException
import org.example.model.InterestingObject
import org.example.redis.RedisService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class LocalCache(
    @Value("\${cache.maximum_size}") val maxSize: Long,
    @Value("\${cache.expire_lru_ms}") val expireLruMs: Long,
    @Value("\${cache.expire_global_ms}") val expireGlobalMs: Long,
    private val service: RedisService
) {
    private val logger = KotlinLogging.logger {}

    private val cache: LoadingCache<Int, InterestingObject> = CacheBuilder.newBuilder()
        .maximumSize(maxSize)
        .expireAfterAccess(expireLruMs, TimeUnit.MILLISECONDS)
        .expireAfterWrite(expireGlobalMs, TimeUnit.MILLISECONDS)
        .build(
            object : CacheLoader<Int, InterestingObject>() {
                override fun load(key: Int): InterestingObject {
                    logger.info { "in load" }
                    return service.read(key)
                }
            }
        )

    fun getValueForKey(key: Int): InterestingObject {
        logger.info { "Trying to get $key from cache" }
        return try {
            cache.get(key)
        } catch (ex: Exception) {
            throw NotFoundException("Not found in cache or redis", ex)
        }
    }
}
