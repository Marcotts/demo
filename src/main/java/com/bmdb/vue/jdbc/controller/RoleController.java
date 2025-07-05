package com.bmdb.vue.jdbc.controller;

import com.bmdb.vue.jdbc.entity.Role;
import com.bmdb.vue.jdbc.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:8080")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public Flux<Map<String, Object>> getAllRoles() {
        return roleService.getAllRolesConfigurable();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Role>> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    public Mono<ResponseEntity<Role>> createRole(@RequestBody Role role) {
        System.out.println("Requête POST reçue pour créer un rôle: " + role);
        return roleService.createRole(role)
            .map(createdRole -> {
                System.out.println("Rôle créé avec succès: " + createdRole);
                return ResponseEntity.ok(createdRole);
            })
            .onErrorResume(e -> {
                System.out.println("Erreur lors de la création du rôle: " + e.getMessage());
                e.printStackTrace();
                return Mono.just(ResponseEntity.status(500)
                    .header("X-Error-Message", e.getMessage())
                    .build());
            });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Role>> updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
        System.out.println("Requête PUT reçue pour mettre à jour le rôle avec ID: " + id);
        // Utiliser la méthode alternative avec DatabaseClient pour plus de fiabilité
        return roleService.updateRoleWithDatabaseClient(id, roleDetails)
                .map(updatedRole -> {
                    System.out.println("Rôle mis à jour avec succès, retournant 200 OK");
                    return ResponseEntity.ok(updatedRole);
                })
                .onErrorResume(e -> {
                    System.out.println("Erreur lors de la mise à jour du rôle: " + e.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRole(@PathVariable Long id) {
        System.out.println("Requête DELETE reçue pour supprimer le rôle avec ID: " + id);
        return roleService.deleteRole(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(e -> {
                    System.out.println("Erreur lors de la suppression du rôle: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(500)
                        .header("X-Error-Message", e.getMessage())
                        .<Void>build());
                });
    }
}
