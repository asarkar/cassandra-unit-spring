package com.asarkar.spring.test.cassandra

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NonCassandraTest {
    @Value("\${cassandra-unit.native-transport-port:-1}")
    private var nativeTransportPort: Int = -1

    @Value("\${cassandra-unit.rpc-port:-1}")
    private var rpcPort: Int = -1

    @Test
    fun testPorts() {
        Assertions.assertEquals(-1, nativeTransportPort)
        Assertions.assertEquals(-1, rpcPort)
    }
}
