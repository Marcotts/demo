
package com.bmdb.vue.jdbc.repository;

import com.bmdb.vue.jdbc.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("local")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindAll() {
        StepVerifier.create(userRepository.findAll())
                .expectNextMatches(user ->
                        "admin".equals(user.getUsername()) &&
                                "admin@example.com".equals(user.getEmail())
                )
                .expectNextMatches(user ->
                        "user1".equals(user.getUsername()) &&
                                "user1@example.com".equals(user.getEmail())
                )
                .expectNextMatches(user ->
                        "user2".equals(user.getUsername()) &&
                                "user2@example.com".equals(user.getEmail())
                )
                .expectNextMatches(user ->
                        "manager".equals(user.getUsername()) &&
                                "manager@example.com".equals(user.getEmail())
                )
                .expectNextMatches(user ->
                        "admin3".equals(user.getUsername()) &&
                                "admin3@example.com".equals(user.getEmail())
                )
                .expectNextMatches(user ->
                        "user3".equals(user.getUsername()) &&
                                "user3@example.com".equals(user.getEmail())
                )
                .verifyComplete();
    }

    @Test
    void testCount() {
        StepVerifier.create(userRepository.count())
                .expectNext(6L)
                .verifyComplete();
    }



    @Test
    void testFindAllWithIds() {
        StepVerifier.create(userRepository.findAll())
                .expectNextMatches(user ->
                        user.getId() == 1L &&
                                "admin".equals(user.getUsername())
                )
                .expectNextMatches(user ->
                        user.getId() == 2L &&
                                "user1".equals(user.getUsername())
                )
                .expectNextMatches(user ->
                        user.getId() == 3L &&
                                "user2".equals(user.getUsername())
                )
                .expectNextMatches(user ->
                        user.getId() == 4L &&
                                "manager".equals(user.getUsername())
                )
                .expectNextMatches(user ->
                        user.getId() == 5L &&
                                "admin3".equals(user.getUsername())
                )
                .expectNextMatches(user ->
                        user.getId() == 6L &&
                                "user3".equals(user.getUsername())
                )
                .verifyComplete();
    }

}