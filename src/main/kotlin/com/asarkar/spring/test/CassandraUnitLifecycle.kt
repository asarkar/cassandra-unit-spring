package com.asarkar.spring.test

import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.cassandraunit.utils.EmbeddedCassandraServerHelper.DEFAULT_CASSANDRA_YML_FILE
import org.cassandraunit.utils.EmbeddedCassandraServerHelper.DEFAULT_LOG4J_CONFIG_FILE
import org.cassandraunit.utils.EmbeddedCassandraServerHelper.DEFAULT_STARTUP_TIMEOUT
import org.cassandraunit.utils.EmbeddedCassandraServerHelper.DEFAULT_TMP_DIR
import org.cassandraunit.utils.EmbeddedCassandraServerHelper.getSession
import org.cassandraunit.utils.EmbeddedCassandraServerHelper.startEmbeddedCassandra
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.SmartLifecycle
import org.springframework.context.annotation.Configuration
import org.springframework.util.ReflectionUtils
import java.io.File
import java.lang.reflect.Field
import java.nio.file.Files
import java.nio.file.Paths

@Configuration(proxyBeanMethods = false)
class CassandraUnitLifecycle : SmartLifecycle {
    companion object {
        private val cassandraDaemon: Field =
            ReflectionUtils.findField(EmbeddedCassandraServerHelper::class.java, "cassandraDaemon")!!
                .apply { ReflectionUtils.makeAccessible(this) }

        val running: Boolean
            get() = cassandraDaemon.get(null) != null
    }

    @Value("\${cassandra-unit.config}")
    var config: String = DEFAULT_CASSANDRA_YML_FILE

    @Value("\${cassandra-unit.timeout}")
    var timeout: Long = DEFAULT_STARTUP_TIMEOUT

    override fun start() {
        if (!running) {
            val configFile = File(config)
            if (configFile.exists()) startEmbeddedCassandra(configFile, timeout)
            else startEmbeddedCassandra(config, timeout)
        }
    }

    override fun stop() {
        if (running) {
            getSession()?.close()
            val log4jConfigFile = DEFAULT_TMP_DIR + DEFAULT_LOG4J_CONFIG_FILE
            Files.deleteIfExists(Paths.get(log4jConfigFile))
        }
    }

    override fun isRunning(): Boolean = running
}
