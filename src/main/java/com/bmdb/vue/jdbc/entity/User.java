package com.bmdb.vue.jdbc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@jakarta.persistence.Table(name = "users")
@Table("users") // Annotation R2DBC pour le nom de la table
public class User {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    // Les champs firstName, lastName et password ne sont pas dans le schéma actuel
    // mais nous les gardons pour une évolution future
    private String firstName;
    private String lastName;
    private String password;

    // Relation avec Role - champ de base de données
    @Column(name = "role_id")
    private Long roleId;

    // Ignorer cette propriété lors de la sérialisation/désérialisation pour éviter les problèmes avec R2DBC
    @JsonIgnore
    @Transient // Indiquer à R2DBC d'ignorer ce champ
    private transient Role role; // Le mot-clé transient garantit que ce champ n'est pas sérialisé

    // Méthode d'aide pour obtenir le nom du rôle
    public String getRoleName() {
        return role != null ? role.getName() : null;
    }
}
