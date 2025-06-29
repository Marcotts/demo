package com.bmdb.vue.jdbc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final DatabaseClient databaseClient;

    public Flux<Map<String, Object>> getAllUsers() {
        return databaseClient.sql("""
            SELECT u.*, r.name as role_name 
            FROM users u 
            JOIN roles r ON u.role_id = r.id
            """)
                .fetch()
                .all();
    }
}
