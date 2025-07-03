package com.bmdb.vue.jdbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("roles")
public class Role {
    @Id
    private Long id;
    private String name;
}