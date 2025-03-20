package com.flowery.status.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class MainServerConfiguration {

    @Value("\${main-server.url}")
    private lateinit var mainServerUrl: String

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}