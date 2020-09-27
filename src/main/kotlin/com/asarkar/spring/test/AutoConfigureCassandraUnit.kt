package com.asarkar.spring.test

import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.springframework.boot.test.autoconfigure.properties.PropertyMapping
import java.lang.annotation.Inherited

/**
 * Annotation for test classes that want to start a Cassandra server as part of the Spring application Context.
 * Note that CassandraUnit can only run one Cassandra instance per JVM; thus, if this annotation is present on
 * more than one test classes, only the first one is used, the others are ignored.
 * @property config the configuration file. Defaults to `org.cassandraunit.utils.EmbeddedCassandraServerHelper.DEFAULT_CASSANDRA_YML_FILE`.
 * @property timeout start timeout in milliseconds. Defaults to `org.cassandraunit.utils.EmbeddedCassandraServerHelper.DEFAULT_STARTUP_TIMEOUT`.
 *
 * @author Abhijit Sarkar
 * @since 1.0.0
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Tags(
    Tag("spring"),
    Tag("spring-boot"),
    Tag("cassandra"),
    Tag("cassandra-unit"),
    Tag("test"),
    Tag("integration-test")
)
@Inherited
@PropertyMapping("cassandra-unit")
annotation class AutoConfigureCassandraUnit(
    val config: String = EmbeddedCassandraServerHelper.DEFAULT_CASSANDRA_YML_FILE,
    val timeout: Long = EmbeddedCassandraServerHelper.DEFAULT_STARTUP_TIMEOUT
)
