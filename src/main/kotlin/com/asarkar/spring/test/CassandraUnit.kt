package com.asarkar.spring.test

import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.springframework.util.ReflectionUtils
import java.io.File
import java.lang.reflect.Field
import java.nio.file.Files
import java.nio.file.Paths

internal object CassandraUnit {
    private val cassandraDaemon: Field =
        ReflectionUtils.findField(EmbeddedCassandraServerHelper::class.java, "cassandraDaemon")!!
            .apply { ReflectionUtils.makeAccessible(this) }

    fun start(config: String, timeout: Long) {
        val configFile = File(config)
        if (configFile.exists()) EmbeddedCassandraServerHelper.startEmbeddedCassandra(configFile, timeout)
        else EmbeddedCassandraServerHelper.startEmbeddedCassandra(config, timeout)
    }

    fun cleanUp() {
        val log4jConfigFile =
            EmbeddedCassandraServerHelper.DEFAULT_TMP_DIR + EmbeddedCassandraServerHelper.DEFAULT_LOG4J_CONFIG_FILE
        Files.deleteIfExists(Paths.get(log4jConfigFile))
    }

    fun isRunning(): Boolean = cassandraDaemon.get(null) != null
}
