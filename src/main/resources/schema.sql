-- Assurez-vous de supprimer les tables si elles existent déjà
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS role_permissions;
DROP TABLE IF EXISTS roles;

-- Création des tables (exemple)
CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       description VARCHAR(255)
);

CREATE TABLE role_permissions (
                       role_id BIGINT,
                       permission VARCHAR(100),
                       PRIMARY KEY (role_id, permission),
                       FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       password VARCHAR(255),
                       role_id BIGINT,
                       FOREIGN KEY (role_id) REFERENCES roles(id)
);
