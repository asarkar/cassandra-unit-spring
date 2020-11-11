package com.asarkar.spring.test.cassandra

import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@AutoConfigureCassandraUnit(config = EmbeddedCassandraServerHelper.CASSANDRA_RNDPORT_YML_FILE)
class AutoConfigureWithRandomPortsTest {
    @Value("\${cassandra-unit.native-transport-port:-1}")
    private var nativeTransportPort: Int = -1

    @Value("\${cassandra-unit.rpc-port:-1}")
    private var rpcPort: Int = -1

    @Test
    fun testPorts() {
        Assertions.assertEquals(EmbeddedCassandraServerHelper.getNativeTransportPort(), nativeTransportPort)
        Assertions.assertEquals(EmbeddedCassandraServerHelper.getRpcPort(), rpcPort)
    }

    @Test
    fun testQuery() {
        val session = EmbeddedCassandraServerHelper.getSession()
        val row = session.execute("SELECT release_version FROM system.local;").one()
        Assertions.assertNotNull(row?.getString("release_version"))
    }
}
