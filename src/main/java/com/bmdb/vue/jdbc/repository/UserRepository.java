package com.bmdb.vue.jdbc.repository;

import com.bmdb.vue.jdbc.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, Long> {
    Mono<User> findByUsername(String username);
    Mono<User> findByEmail(String email);
    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsByEmail(String email);
}