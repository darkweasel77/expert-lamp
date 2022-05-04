package org.example.redis

import io.lettuce.core.api.sync.RedisCommands
import mu.KotlinLogging
import org.example.model.InterestingObject
import org.example.model.NotFoundException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class RedisService(
    @Qualifier("redis-commands")
    private val redisCommands: RedisCommands<String, String>
) {

    private val logger = KotlinLogging.logger {}

    fun write(interestingObject: InterestingObject) {
        this.redisCommands.set(interestingObject.id.toString(), interestingObject.value)
    }

    fun read(id: Int): InterestingObject {
        logger.info { "reading $id from redis" }
        try {
            val first = redisCommands.get(id.toString())
            logger.info { "first = $first" }
            return InterestingObject(id, first)
        } catch (ex: Exception) {
            logger.info { "${ex.message}" }
            throw NotFoundException("Could not find $id", ex)
        }
    }
}
