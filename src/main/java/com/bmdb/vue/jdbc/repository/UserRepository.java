package com.bmdb.vue.jdbc.repository;

import com.bmdb.vue.jdbc.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface UserRepository extends R2dbcRepository<User, Long> {
}