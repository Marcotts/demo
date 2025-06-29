package com.bmdb.vue.jdbc.loaddata;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final DatabaseClient databaseClient;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initializeData()
                .doOnSuccess(v -> System.out.println("✅ Données initiales chargées avec succès"))
                .doOnError(e -> System.err.println("❌ Erreur lors de l'initialisation des données: " + e.getMessage()))
                .subscribe();
    }

    private Mono<Void> initializeData() {
        return insertRoles()
                .then(insertUsers());
    }

    private Mono<Void> insertRoles() {
        return databaseClient.sql("INSERT INTO roles (name) VALUES ($1)")
                .bind(0, "ADMIN")
                .then()
                .then(databaseClient.sql("INSERT INTO roles (name) VALUES ($1)")
                        .bind(0, "USER")
                        .then());
    }

    private Mono<Void> insertUsers() {
        return databaseClient.sql("""
            INSERT INTO users (username, email, role_id)
            VALUES ($1, $2, (SELECT id FROM roles WHERE name = $3))
            """)
                .bind(0, "admin")
                .bind(1, "admin@example.com")
                .bind(2, "ADMIN")
                .then()
                .then(databaseClient.sql("""
                INSERT INTO users (username, email, role_id)
                VALUES ($1, $2, (SELECT id FROM roles WHERE name = $3))
                """)
                        .bind(0, "user")
                        .bind(1, "user@example.com")
                        .bind(2, "USER")
                        .then());
    }
}
