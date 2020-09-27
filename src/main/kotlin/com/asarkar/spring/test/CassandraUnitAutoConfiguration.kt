package com.asarkar.spring.test

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class CassandraUnitAutoConfiguration {
    @Bean
    fun cassandraUnitLifecycle(): CassandraUnitLifecycle {
        return CassandraUnitLifecycle()
    }
}
