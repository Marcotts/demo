package com.bmdb.vue.jdbc.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class R2dbcConfig {
    @Bean
    public ApplicationRunner runner(ConnectionFactory connectionFactory) {
        return args -> {
            Mono.from(connectionFactory.create())
                    .flatMap(connection ->
                            Mono.from(connection.createStatement("SELECT 1")
                                            .execute())
                                    .doFinally(st -> Mono.from(connection.close()).subscribe())
                    )
                    .doOnSuccess(result -> System.out.println("✅ Connexion à la base de données réussie !"))
                    .doOnError(error -> System.err.println("❌ Erreur de connexion : " + error.getMessage()))
                    .subscribe();
        };
    }
}
