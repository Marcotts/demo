package com.bmdb.vue.jdbc.loaddata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private static final String COLOR_GREEN_SUCCESS = "\u001B[32m✅\u001B[0m";
    private static final String COLOR_RED_ERROR = "\u001B[31m❌\u001B[0m";


    private static final String PREFIX_SUCCESS = "[SUCCESS]";
    private static final String PREFIX_ERROR = "[ERROR]";

    private final DatabaseClient databaseClient;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initializeData()
                .doOnSuccess(v -> {
                    System.out.println("✅ Données initiales chargées avec succès");
                    log.info("{}{} Données initiales chargées avec succès",PREFIX_SUCCESS, COLOR_GREEN_SUCCESS);
                })
                .doOnError(e ->{
                    System.err.println("❌ Erreur lors de l'initialisation des données: " + e.getMessage());
                    log.error("{}{} Erreur lors de l'initialisation des données: {}",PREFIX_ERROR,COLOR_RED_ERROR, e.getMessage());
                })
                .subscribe();
    }

    private Mono<Void> initializeData() {
        return insertRoles()
                .then(insertUsers());
    }

    private Mono<Void> insertRoles() {
        return databaseClient.sql("INSERT INTO roles (name) VALUES ($1)")
                .bind(0, "ADMIN2")
                .then()
                .then(databaseClient.sql("INSERT INTO roles (name) VALUES ($1)")
                        .bind(0, "USER2")
                        .then());
    }

    private Mono<Void> insertUsers() {
        return databaseClient.sql("""
            INSERT INTO users (username, email, role_id)
            VALUES ($1, $2, (SELECT id FROM roles WHERE name = $3))
            """)
                .bind(0, "admin2")
                .bind(1, "admin2@example.com")
                .bind(2, "ADMIN2")
                .then()
                .then(databaseClient.sql("""
                INSERT INTO users (username, email, role_id)
                VALUES ($1, $2, (SELECT id FROM roles WHERE name = $3))
                """)
                        .bind(0, "user2")
                        .bind(1, "user2@example.com")
                        .bind(2, "USER2")
                        .then());
    }
}
