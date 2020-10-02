# cassandra-unit-spring

Starts the Cassandra server and makes the ports available as Spring Boot environment properties.

Requires Java 8 or later. Uses [cassandra-unit](https://github.com/jsevellec/cassandra-unit) and [Spring Boot](https://spring.io/projects/spring-boot). 

## Installation

You can find the latest version on Bintray. [ ![Download](https://api.bintray.com/packages/asarkar/mvn/com.asarkar.spring%3Acassandra-unit-spring/images/download.svg) ](https://bintray.com/asarkar/mvn/com.asarkar.spring%3Acassandra-unit-spring/_latestVersion)

It is also on Maven Central and jcenter.

## Usage

The only thing you need is the `AutoConfigureCassandraUnit` annotation:

```
@SpringBootTest
@AutoConfigureCassandraUnit(config = EmbeddedCassandraServerHelper.CASSANDRA_RNDPORT_YML_FILE)
public class AutoConfigureWithRandomPortsTest {
    @Value("${cassandra-unit.native-transport-port:-1}")
    private int nativeTransportPort = -1;

    @Value("${cassandra-unit.rpc-port:-1}")
    private int rpcPort = -1;

    @Test
    public void testUseRandomPorts() {
        Assertions.assertEquals(EmbeddedCassandraServerHelper.getNativeTransportPort(), nativeTransportPort);
        Assertions.assertEquals(EmbeddedCassandraServerHelper.getRpcPort(), rpcPort);
    }
}
```

See KDoc for more details.

Note that CassandraUnit can only run one Cassandra instance per JVM; thus, if `AutoConfigureCassandraUnit` annotation 
is present on more than one test classes, only the first one is used, the others are ignored. That means the port
properties will not change once a Cassandra instance is started.

This library aims to be minimal and manages only the lifecycle of the Cassandra server during testing; it does not 
run initialization scripts or clean the database between tests, because you can do those things yourself.

> If you abort a test or run two tests both of which start the server, you may be faced with a `FileAlreadyExistsException`. That is because of [this bug](https://github.com/jsevellec/cassandra-unit/issues/319). If using Maven, `clean` goal will delete the temporary directory; if using Gradle, you can either delete the `target` directory manually, or [add it to the Gradle `clean` task](https://stackoverflow.com/a/29813360/839733).
## Contribute

This project is a volunteer effort. You are welcome to send pull requests, ask questions, or create issues.
If you like it, you can help by spreading the word!

## License

Copyright 2020 Abhijit Sarkar - Released under [Apache License v2.0](LICENSE).
