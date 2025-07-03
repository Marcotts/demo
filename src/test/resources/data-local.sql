-- Nettoyage des données existantes (optionnel, selon vos besoins)
DELETE FROM users;
DELETE FROM roles;

-- Réinitialisation des séquences si nécessaire pour H2
ALTER TABLE roles ALTER COLUMN id RESTART WITH 1;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;


INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER'),
       ('MANAGER'),
       ('GUEST');

INSERT INTO users (username, email, role_id)
VALUES ('admin', 'admin@example.com', (SELECT id FROM roles WHERE name = 'ADMIN')),
       ('user1', 'user1@example.com', (SELECT id FROM roles WHERE name = 'USER')),
       ('user2', 'user2@example.com', (SELECT id FROM roles WHERE name = 'USER')),
       ('manager', 'manager@example.com', (SELECT id FROM roles WHERE name = 'MANAGER'));
