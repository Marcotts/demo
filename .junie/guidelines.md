# Development Guidelines for Vue.js Demo Project

This document provides essential information for developers working on this project.

## Build/Configuration Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Node.js and npm (for frontend development)

### Backend Setup
1. Clone the repository
2. Build the project using Maven:
   ```
   mvn clean install
   ```
3. Run the application:
   ```
   mvn spring-boot:run
   ```

### Frontend Setup
The frontend is a Vue.js application located in the `frontend` directory. The build process automatically copies the frontend build to the static resources directory of the Spring Boot application.

1. Navigate to the frontend directory:
   ```
   cd frontend
   ```
2. Install dependencies:
   ```
   npm install
   ```
3. Run the development server:
   ```
   npm run serve
   ```
4. Build for production:
   ```
   npm run build
   ```

## Testing Information

### Testing Framework
The project uses:
- JUnit 5 (Jupiter) for unit testing
- Spring Boot Test for integration testing
- Reactor Test for testing reactive components

### Test Configuration
- Tests use the `local` profile by default, configured in `src/test/resources/application-local.properties`
- An H2 in-memory database is used for testing
- Database schema is defined in `src/test/resources/schema.sql`
- Test data is loaded from `src/test/resources/data.sql`

### Running Tests
1. Run all tests:
   ```
   mvn test
   ```
2. Run a specific test class:
   ```
   mvn test -Dtest=UserRepositoryTest
   ```
3. Run a specific test method:
   ```
   mvn test -Dtest=UserRepositoryTest#testFindAll
   ```

### Creating New Tests
1. Create a new test class in the appropriate package under `src/test/java`
2. Use the `@SpringBootTest` annotation for integration tests
3. Use the `@ActiveProfiles("local")` annotation to use the local profile
4. For repository tests, use `StepVerifier` to test reactive streams

#### Example: Repository Test
```java
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
}
```

## Additional Development Information

### Project Structure
- `src/main/java`: Java source code
- `src/main/resources`: Configuration files and static resources
- `src/test/java`: Test source code
- `src/test/resources`: Test configuration and data
- `frontend`: Vue.js frontend application

### Key Technologies
- Spring Boot 3.5.3
- Spring WebFlux for reactive web
- Spring Data R2DBC for reactive database access
- Spring Data JPA for traditional ORM
- H2 Database for development and testing
- Vue.js for frontend

### Database Configuration
The project uses both R2DBC (reactive) and JPA (traditional) for database access. Entity classes are annotated with both R2DBC and JPA annotations.

### Reactive Programming
The backend uses reactive programming with Project Reactor. Key points:
- Use `Mono` for single results
- Use `Flux` for multiple results
- Use `StepVerifier` for testing reactive streams

### Entity Structure
Entities have both JPA and R2DBC annotations:
- `@Entity` and `@jakarta.persistence.Table` for JPA
- `@Table` for R2DBC
- `@Id` annotations for both frameworks

### Troubleshooting
- If you encounter SQL grammar errors in tests, check that the entity fields match the database schema
- For reactive tests, ensure you're using `StepVerifier` correctly to test the reactive streams
- If frontend changes aren't reflected in the application, ensure the frontend build is copied to the static resources directory

## Note on RoleRepositoryTest

The `RoleRepositoryTest.java` file was initially created as an example test to demonstrate the testing process, but was temporarily removed during a cleanup phase. It has been restored as it provides valuable test coverage for the `RoleRepository` class, ensuring that:

1. All roles can be retrieved correctly
2. The count of roles is accurate
3. The existence check by role name functions properly

This test should be maintained as part of the test suite to ensure the continued functionality of the role management system.
