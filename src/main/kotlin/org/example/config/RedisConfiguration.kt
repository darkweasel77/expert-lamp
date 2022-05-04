package org.example.config

import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands
import io.lettuce.core.resource.ClientResources
import org.example.model.RedisServerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedisConfiguration() {
    @Bean("redis-commands")
    fun redisCommands(redisClient: RedisClient): RedisCommands<String, String> {
        return redisClient.connect().sync()
    }

    @Bean("redis-client")
    fun redisClient(redisServerConfig: RedisServerConfig): RedisClient? {
        return RedisClient.create(
            ClientResources.builder().build(),
            "redis://${redisServerConfig.redisHost}:${redisServerConfig.redisPort}"
        )
    }
}
