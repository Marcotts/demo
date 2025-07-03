package com.bmdb.vue.jdbc.repository;

import com.bmdb.vue.jdbc.entity.Role;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RoleRepository extends R2dbcRepository<Role, Long> {
}