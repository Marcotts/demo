package com.bmdb.vue.jdbc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@jakarta.persistence.Table(name = "roles")
@Table("roles") // Annotation R2DBC pour le nom de la table
public class Role {
    @jakarta.persistence.Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    // Les annotations JPA ne fonctionnent pas avec R2DBC
    // Nous gérons cette relation manuellement dans le service
    @Transient // Indiquer à R2DBC d'ignorer ce champ pour la persistance
    private List<String> permissions;
}
