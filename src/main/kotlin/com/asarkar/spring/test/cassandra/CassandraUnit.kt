package com.asarkar.spring.test.cassandra

import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.springframework.util.ReflectionUtils
import java.io.File
import java.lang.reflect.Field
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Utility to start the server and clean up any files left behind.
 *
 * @author Abhijit Sarkar
 * @since 1.0.5
 */
internal object CassandraUnit {
    private val cassandraDaemon: Field =
        ReflectionUtils.findField(EmbeddedCassandraServerHelper::class.java, "cassandraDaemon")!!
            .apply { ReflectionUtils.makeAccessible(this) }

    fun start(config: String, timeout: Long) {
        val configFile = File(config)
        if (configFile.exists()) EmbeddedCassandraServerHelper.startEmbeddedCassandra(configFile, timeout)
        else EmbeddedCassandraServerHelper.startEmbeddedCassandra(config, timeout)
    }

    // https://github.com/jsevellec/cassandra-unit/issues/319
    fun cleanUp() {
        val log4jConfigFile =
            EmbeddedCassandraServerHelper.DEFAULT_TMP_DIR + EmbeddedCassandraServerHelper.DEFAULT_LOG4J_CONFIG_FILE
        Files.deleteIfExists(Paths.get(log4jConfigFile))
    }

    // https://github.com/jsevellec/cassandra-unit/issues/318
    fun isRunning(): Boolean = cassandraDaemon.get(null) != null
}
