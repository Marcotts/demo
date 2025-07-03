-- Assurez-vous de supprimer les tables si elles existent déjà
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- Création des tables (exemple)
CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL
);

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       role_id BIGINT,
                       FOREIGN KEY (role_id) REFERENCES roles(id)
);
