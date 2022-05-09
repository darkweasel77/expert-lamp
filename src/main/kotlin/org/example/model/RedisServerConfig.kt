package org.example.model

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class RedisServerConfig(
    @Value("\${spring.redis.port}") val redisPort: Int,
    @Value("\${spring.redis.host}") val redisHost: String
)
