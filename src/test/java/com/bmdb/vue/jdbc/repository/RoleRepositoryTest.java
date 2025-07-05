package com.bmdb.vue.jdbc.repository;

import com.bmdb.vue.jdbc.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("local")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindAll() {
        StepVerifier.create(roleRepository.findAll())
                .expectNextMatches(role -> 
                        "ADMIN".equals(role.getName())
                )
                .expectNextMatches(role -> 
                        "USER".equals(role.getName())
                )
                .expectNextMatches(role -> 
                        "MANAGER".equals(role.getName())
                )
                .expectNextMatches(role -> 
                        "GUEST".equals(role.getName())
                )
                .verifyComplete();
    }

    @Test
    void testCount() {
        StepVerifier.create(roleRepository.count())
                .expectNext(4L)
                .verifyComplete();
    }

    @Test
    void testExistsByName() {
        StepVerifier.create(roleRepository.existsByName("ADMIN"))
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(roleRepository.existsByName("NONEXISTENT_ROLE"))
                .expectNext(false)
                .verifyComplete();
    }
}