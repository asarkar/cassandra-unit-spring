package com.asarkar.spring.test

import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationPreparedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.env.MapPropertySource
import org.springframework.util.SocketUtils
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.emitter.Emitter
import org.yaml.snakeyaml.events.ScalarEvent
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths

@Order(Ordered.LOWEST_PRECEDENCE)
class CassandraUnitApplicationListener : ApplicationListener<ApplicationPreparedEvent> {
    private val log = LoggerFactory.getLogger(CassandraUnitApplicationListener::class.java)
    private val prefix = "cassandra-unit"

    override fun onApplicationEvent(event: ApplicationPreparedEvent) {
        val env = event.applicationContext.environment
        val config = env.getProperty("$prefix.config", String::class.java) ?: return
        val cassandra = if (CassandraUnitLifecycle.running) {
            mapOf(
                "$prefix.native-transport-port" to EmbeddedCassandraServerHelper.getNativeTransportPort(),
                "$prefix.rpc-port" to EmbeddedCassandraServerHelper.getRpcPort()
            )
        } else {
            val outFile = config.takeIf { it != EmbeddedCassandraServerHelper.DEFAULT_CASSANDRA_YML_FILE }
                ?.let { this.createTempFile(it) }
            discoverPorts(config, outFile).also {
                it["$prefix.config"] = outFile ?: config
            }
        }

        log.debug("Adding CassandraUnit properties to environment: {}", cassandra)
        with(env.propertySources) {
            remove("cassandra")
            addFirst(MapPropertySource("cassandra", cassandra))
        }
    }

    private fun discoverPorts(config: String, outFile: String?): MutableMap<String, Any> {
        val input = EmbeddedCassandraServerHelper::class.java.classLoader
            .getResourceAsStream(config) ?: throw IllegalArgumentException("Couldn't find $config")
        val cassandra = mutableMapOf<String, Any>()
        input.reader().use { reader ->
            outFile?.let { FileWriter(it) }.use { writer ->
                val emitter = writer?.let { Emitter(it, DumperOptions()) }
                var portName: String? = null
                Yaml().parse(reader)
                    .forEach {
                        if (it !is ScalarEvent) emitter?.emit(it)
                        else {
                            val (name, value) = getPortNameAndValue(it, portName)
                            if (value != null) {
                                cassandra["$prefix.$portName"] = value
                                emitter?.emit(it.withValue(value))
                            } else emitter?.emit(it)
                            portName = name
                        }
                    }
            }
        }
        return cassandra
    }

    private fun getPortNameAndValue(event: ScalarEvent, portName: String?): Pair<String?, Int?> {
        if (portName == null && event.value.endsWith("_port")) {
            return event.value.replace('_', '-') to null
        } else if (portName != null) {
            val port = if (event.value.matches("0+".toRegex())) SocketUtils.findAvailableTcpPort()
            else event.value.toInt()
            return null to port
        }
        return portName to null
    }

    private fun createTempFile(config: String): String {
        val tmpDir = System.getProperty("java.io.tmpdir")
        val tmpFile = Paths.get(tmpDir).resolve(config)
        Files.deleteIfExists(tmpFile)
        Files.createFile(tmpFile)

        return tmpFile.toString()
    }

    private fun ScalarEvent.withValue(value: Int): ScalarEvent {
        return ScalarEvent(
            anchor,
            tag,
            implicit,
            value.toString(),
            startMark,
            endMark,
            scalarStyle
        )
    }
}
