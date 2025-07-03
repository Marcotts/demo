package com.bmdb.vue.jdbc;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("local")
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

    @Test
    void testTableCreation() {
        Mono<Connection> connectionMono = Mono.from(connectionFactory.create());

        StepVerifier.create(
                        connectionMono.flatMap(connection ->
                                Mono.from(connection.createStatement("SELECT COUNT(*) FROM users")
                                                .execute())
                                        .doFinally(st -> Mono.from(connection.close()).subscribe())
                        )
                )
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testDataInitialization() {
        Mono<Connection> connectionMono = Mono.from(connectionFactory.create());

        StepVerifier.create(
                        connectionMono.flatMap(connection ->
                                Mono.from(connection.createStatement(
                                                        "SELECT COUNT(*) FROM users WHERE role_id = (SELECT id FROM roles WHERE name = 'ADMIN')")
                                                .execute())
                                        .doFinally(st -> Mono.from(connection.close()).subscribe())
                        )
                )
                .expectNextMatches(result -> Mono.from(result.map((row, metadata) ->
                        row.get(0, Long.class))).block() == 1)
                .verifyComplete();
    }


}
