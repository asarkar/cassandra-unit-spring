package com.asarkar.spring.test

import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(
    name = [
        "org.cassandraunit.utils.EmbeddedCassandraServerHelper", "com.datastax.oss.driver.api.core.CqlSession"
    ]
)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
class CassandraUnitAutoConfiguration {
    @Bean
    fun cassandraUnitLifecycle(): CassandraUnitLifecycle {
        return CassandraUnitLifecycle().apply { start() }
    }
}
