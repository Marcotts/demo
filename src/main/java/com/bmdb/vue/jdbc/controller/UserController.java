package com.bmdb.vue.jdbc.controller;

import com.bmdb.vue.jdbc.entity.User;
import com.bmdb.vue.jdbc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Flux<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    public Mono<User> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        System.out.println("Requête PUT reçue pour mettre à jour l'utilisateur avec ID: " + id);
        return userService.updateUser(id, userDetails)
                .map(updatedUser -> {
                    System.out.println("Utilisateur mis à jour avec succès, retournant 200 OK");
                    return ResponseEntity.ok(updatedUser);
                })
                .onErrorResume(e -> {
                    System.out.println("Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()));
    }
}
