package com.bmdb.vue.jdbc.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class R2dbcConfig {

    private static final String COLOR_GREEN_SUCCESS = "\u001B[32m✅\u001B[0m";
    private static final String COLOR_RED_ERROR = "\u001B[31m❌\u001B[0m";


    private static final String PREFIX_SUCCESS = "[SUCCESS]";
    private static final String PREFIX_ERROR = "[ERROR]";

    @Bean
    public ApplicationRunner runner(ConnectionFactory connectionFactory) {
        return args -> {
            Mono.from(connectionFactory.create())
                    .flatMap(connection ->
                            Mono.from(connection.createStatement("SELECT 1")
                                            .execute())
                                    .doFinally(st -> Mono.from(connection.close()).subscribe())
                    )
                    .doOnSuccess(result ->
                        {   System.out.println("✅ Connexion à la base de données réussie !");
                            log.info("{}{} Connexion à la base de données réussie !", PREFIX_SUCCESS, COLOR_GREEN_SUCCESS);
                        })
                    .doOnError(error ->
                        {   System.err.println("❌ Erreur de connexion : " + error.getMessage());
                            log.error("{}{} Erreur de connexion : {} " ,PREFIX_ERROR,COLOR_RED_ERROR, error.getMessage());
                        })
                    .subscribe();
        };
    }
}
