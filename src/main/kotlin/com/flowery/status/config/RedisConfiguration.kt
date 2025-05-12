package com.flowery.status.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
class RedisConfiguration {

    @Value("\${spring.data.redis.host}")
    private lateinit var redisHost: String

    @Value("\${spring.data.redis.port}")
    private var redisPort: Int = 0

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisConfig = RedisStandaloneConfiguration(redisHost, redisPort)
        return LettuceConnectionFactory(redisConfig)
    }

//    @Bean
//    fun redisTemplate(): RedisTemplate<String, String> {
//        val template = RedisTemplate<String, String>()
//        template.connectionFactory = redisConnectionFactory()
//        template.keySerializer = StringRedisSerializer()
//        template.valueSerializer = StringRedisSerializer()
//        return template
//    }
}