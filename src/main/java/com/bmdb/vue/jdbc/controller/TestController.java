package com.bmdb.vue.jdbc.controller;

import com.bmdb.vue.jdbc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
    private final UserService userService;

    @GetMapping("/users")
    public Flux<Map<String, Object>> getUsers() {
        return userService.getAllUsers();
    }
}
