package com.bmdb.vue.jdbc.service;

import com.bmdb.vue.jdbc.entity.Role;
import com.bmdb.vue.jdbc.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    private DatabaseClient databaseClient;

    public Flux<Role> getAllRoles() {
        // Utiliser la table 'roles' au lieu de 'ROLE'
        return roleRepository.findAll();
    }

    public Mono<Role> getRoleById(Long id) {
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Role not found")));
    }

    public Mono<Role> createRole(Role role) {
        System.out.println("Tentative de création d'un rôle: " + role);
        // Extraire les permissions avant de sauvegarder le rôle
        java.util.List<String> permissions = role.getPermissions();
        role.setPermissions(null); // Supprimer les permissions pour éviter les erreurs

        return roleRepository.save(role)
            .flatMap(savedRole -> {
                System.out.println("Rôle créé avec ID: " + savedRole.getId());
                if (permissions != null && !permissions.isEmpty()) {
                    System.out.println("Ajout de " + permissions.size() + " permissions au rôle");
                    return updateRolePermissions(savedRole.getId(), permissions)
                        .then(Mono.defer(() -> {
                            // Recharger le rôle avec ses permissions
                            return getRoleById(savedRole.getId());
                        }));
                } else {
                    System.out.println("Aucune permission à ajouter");
                    return Mono.just(savedRole);
                }
            })
            .doOnError(error -> {
                System.out.println("Erreur lors de la création du rôle: " + error.getMessage());
                error.printStackTrace();
            });
    }

    public Mono<Role> updateRole(Long id, Role roleDetails) {
        System.out.println("Tentative de mise à jour du rôle avec ID: " + id);
        return roleRepository.findById(id)
                .doOnNext(role -> System.out.println("Rôle trouvé: " + role))
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.println("Rôle avec ID " + id + " non trouvé");
                    return Mono.error(new RuntimeException("Role not found with id: " + id));
                }))
                .flatMap(role -> {
                    role.setName(roleDetails.getName());
                    role.setDescription(roleDetails.getDescription());
                    role.setPermissions(roleDetails.getPermissions());
                    System.out.println("Mise à jour du rôle: " + role);
                    return roleRepository.save(role)
                        .doOnSuccess(savedRole -> System.out.println("Rôle mis à jour avec succès: " + savedRole))
                        .doOnError(error -> System.out.println("Erreur lors de la sauvegarde du rôle: " + error.getMessage()));
                });
            }

            // Méthode alternative utilisant directement DatabaseClient
            public Mono<Role> updateRoleWithDatabaseClient(Long id, Role roleDetails) {
        System.out.println("Tentative de mise à jour du rôle avec DatabaseClient, ID: " + id);
        return databaseClient.sql("UPDATE roles SET name = :name, description = :description WHERE id = :id")
                .bind("name", roleDetails.getName())
                .bind("description", roleDetails.getDescription())
                .bind("id", id)
                .fetch().rowsUpdated()
                .flatMap(rowsUpdated -> {
                    System.out.println("Nombre de lignes mises à jour: " + rowsUpdated);
                    if (rowsUpdated > 0) {
                        // Mise à jour des permissions
                            java.util.List<String> permissions = roleDetails.getPermissions();
                            return updateRolePermissions(id, permissions)
                                .then(getRoleById(id));
                    } else {
                        return Mono.error(new RuntimeException("Role not found with id: " + id));
                    }
                });
            }

        private Mono<Void> updateRolePermissions(Long roleId, java.util.List<String> permissions) {
        // D'abord supprimer toutes les permissions existantes
        return databaseClient.sql("DELETE FROM role_permissions WHERE role_id = :roleId")
                .bind("roleId", roleId)
                .fetch().rowsUpdated()
                .then(Mono.defer(() -> {
                    if (permissions == null || permissions.isEmpty()) {
                        return Mono.empty();
                    }

                    // Ajouter les nouvelles permissions
                    java.util.List<Mono<Long>> insertOperations = permissions.stream()
                            .map(permission -> databaseClient.sql("INSERT INTO role_permissions (role_id, permission) VALUES (:roleId, :permission)")
                                    .bind("roleId", roleId)
                                    .bind("permission", permission)
                                    .fetch().rowsUpdated())
                            .collect(java.util.stream.Collectors.toList());

                    return Flux.concat(insertOperations).then();
                }));
    }

    public Mono<Void> deleteRole(Long id) {
        System.out.println("Tentative de suppression du rôle avec ID: " + id);
        // Vérifier d'abord si le rôle existe
        return roleRepository.findById(id)
            .switchIfEmpty(Mono.error(new RuntimeException("Role not found with id: " + id)))
            .flatMap(role -> {
                // Étape 1: Supprimer les références utilisateur (mettre role_id à NULL)
                System.out.println("Suppression des références utilisateur pour le rôle ID: " + id);
                return databaseClient.sql("UPDATE users SET role_id = NULL WHERE role_id = :roleId")
                    .bind("roleId", id)
                    .fetch().rowsUpdated()
                    .doOnNext(count -> System.out.println(count + " utilisateurs mis à jour"))
                    // Étape 2: Supprimer les permissions du rôle
                    .then(Mono.defer(() -> {
                        System.out.println("Suppression des permissions pour le rôle ID: " + id);
                        return databaseClient.sql("DELETE FROM role_permissions WHERE role_id = :roleId")
                            .bind("roleId", id)
                            .fetch().rowsUpdated()
                            .doOnNext(count -> System.out.println(count + " permissions supprimées"));
                    }))
                    // Étape 3: Supprimer le rôle lui-même
                    .then(Mono.defer(() -> {
                        System.out.println("Suppression du rôle ID: " + id);
                        return roleRepository.deleteById(id)
                            .doOnSuccess(v -> System.out.println("Rôle supprimé avec succès"))
                            .doOnError(e -> System.out.println("Erreur lors de la suppression du rôle: " + e.getMessage()));
                    }));
            });
    }

    public Flux<Map<String, Object>> getAllRolesConfigurable() {
        return databaseClient.sql("SELECT r.id, r.name, r.description FROM roles r")
                .fetch()
                .all()
                .flatMap(row -> {
                    Long roleId = (Long) row.get("ID");
                    return getPermissionsForRole(roleId)
                            .collectList()
                            .map(permissions -> {
                                Map<String, Object> formattedRow = new java.util.HashMap<>();
                                formattedRow.put("id", roleId);
                                formattedRow.put("name", row.get("NAME"));
                                formattedRow.put("description", row.get("DESCRIPTION") != null ? row.get("DESCRIPTION") : "");
                                formattedRow.put("permissions", permissions);
                                return formattedRow;
                            });
                });
    }

    private Flux<String> getPermissionsForRole(Long roleId) {
        return databaseClient.sql("SELECT permission FROM role_permissions WHERE role_id = :roleId")
                .bind("roleId", roleId)
                .fetch()
                .all()
                .map(row -> {
                    // Essayer d'abord avec le nom de colonne en majuscules (H2 par défaut)
                    String permission = (String) row.get("PERMISSION");
                    if (permission == null) {
                        // Si null, essayer avec le nom en minuscules
                        permission = (String) row.get("permission");
                    }
                    return permission;
                })
                .doOnError(error -> {
                    System.out.println("Erreur lors de la récupération des permissions: " + error.getMessage());
                    error.printStackTrace();
                });
    }
}
