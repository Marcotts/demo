package com.bmdb.vue.jdbc.service;

import com.bmdb.vue.jdbc.entity.Role;
import com.bmdb.vue.jdbc.entity.User;
import com.bmdb.vue.jdbc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final DatabaseClient databaseClient;
    private final UserRepository userRepository;

    public Flux<Map<String, Object>> getAllUsersConfigurable() {
        return databaseClient.sql("""
            SELECT u.*, r.name as role_name 
            FROM users u 
            JOIN roles r ON u.role_id = r.id
            """)
                .fetch()
                .all();
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> getUserById(Long id) {
        // Utiliser DatabaseClient pour contrôler précisément les champs retournés
        return databaseClient.sql(
                "SELECT u.id, u.username, u.email, u.first_name, u.last_name, u.password, u.role_id, " +
                "r.name as role_name, r.description as role_description " +
                "FROM users u " +
                "LEFT JOIN roles r ON u.role_id = r.id " +
                "WHERE u.id = :id")
                .bind("id", id)
                .map((row, metadata) -> {
                    User user = new User();
                    user.setId(row.get("id", Long.class));
                    user.setUsername(row.get("username", String.class));
                    user.setEmail(row.get("email", String.class));
                    user.setFirstName(row.get("first_name", String.class));
                    user.setLastName(row.get("last_name", String.class));
                    user.setPassword(row.get("password", String.class));
                    user.setRoleId(row.get("role_id", Long.class));

                    // Créer l'objet Role si nécessaire mais ne pas le définir dans user
                    // pour éviter les problèmes de sérialisation
                    String roleName = row.get("role_name", String.class);
                    if (roleName != null) {
                        Role role = new Role();
                        role.setId(user.getRoleId());
                        role.setName(roleName);
                        role.setDescription(row.get("role_description", String.class));
                        // Ne pas définir user.setRole(role) pour éviter les problèmes avec R2DBC
                    }

                    return user;
                })
                .one()
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + id)));
    }

    public Mono<User> createUser(User user) {
        // Ici vous pourriez ajouter le hachage du mot de passe
        return userRepository.save(user);
    }

    public Mono<User> updateUser(Long id, User userDetails) {
        System.out.println("Tentative de mise à jour de l'utilisateur avec ID: " + id);

        // Utiliser DatabaseClient pour une mise à jour plus directe
        return userRepository.findById(id)
                .doOnNext(user -> System.out.println("Utilisateur trouvé: " + user))
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.println("Utilisateur avec ID " + id + " non trouvé");
                    return Mono.error(new RuntimeException("User not found with id: " + id));
                }))
                .flatMap(existingUser -> {
                    // Construire la requête SQL avec uniquement les champs non nuls
                    StringBuilder sql = new StringBuilder("UPDATE users SET ");
                    java.util.List<String> setClauses = new java.util.ArrayList<>();

                    if (userDetails.getUsername() != null) {
                        setClauses.add("username = :username");
                    }
                    if (userDetails.getEmail() != null) {
                        setClauses.add("email = :email");
                    }
                    if (userDetails.getFirstName() != null) {
                        setClauses.add("first_name = :firstName");
                    }
                    if (userDetails.getLastName() != null) {
                        setClauses.add("last_name = :lastName");
                    }
                    if (userDetails.getPassword() != null) {
                        setClauses.add("password = :password");
                    }
                    if (userDetails.getRoleId() != null) {
                        setClauses.add("role_id = :roleId");
                    }

                    sql.append(String.join(", ", setClauses));
                    sql.append(" WHERE id = :id");

                    System.out.println("SQL généré: " + sql.toString());

                    // Créer la requête avec les bons bindings
                    DatabaseClient.GenericExecuteSpec execSpec = databaseClient.sql(sql.toString())
                            .bind("id", id);

                    if (userDetails.getUsername() != null) {
                        execSpec = execSpec.bind("username", userDetails.getUsername());
                    }
                    if (userDetails.getEmail() != null) {
                        execSpec = execSpec.bind("email", userDetails.getEmail());
                    }
                    if (userDetails.getFirstName() != null) {
                        execSpec = execSpec.bind("firstName", userDetails.getFirstName());
                    }
                    if (userDetails.getLastName() != null) {
                        execSpec = execSpec.bind("lastName", userDetails.getLastName());
                    }
                    if (userDetails.getPassword() != null) {
                        execSpec = execSpec.bind("password", userDetails.getPassword());
                    }
                    if (userDetails.getRoleId() != null) {
                        execSpec = execSpec.bind("roleId", userDetails.getRoleId());
                    }

                    return execSpec.fetch().rowsUpdated()
                        .flatMap(rowsUpdated -> {
                            System.out.println("Nombre de lignes mises à jour: " + rowsUpdated);
                            if (rowsUpdated > 0) {
                                return getUserById(id);
                            } else {
                                return Mono.error(new RuntimeException("User not found or no change made"));
                            }
                        });
                });
    }

    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }
}
