package com.bmdb.vue.jdbc;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class DatabaseConnectionTest {
    @Autowired
    private ConnectionFactory connectionFactory;

    @Test
    void testDatabaseConnection() {
        Mono<Connection> connectionMono = Mono.from(connectionFactory.create());

        StepVerifier.create(connectionMono)
                .expectNextCount(1)
                .verifyComplete();
    }
}
