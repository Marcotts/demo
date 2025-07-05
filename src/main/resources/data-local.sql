-- Nettoyage des données existantes (optionnel, selon vos besoins)
DELETE FROM users;
DELETE FROM roles;

-- Réinitialisation des séquences si nécessaire pour H2
ALTER TABLE roles ALTER COLUMN id RESTART WITH 1;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;


INSERT INTO roles (name, description)
VALUES ('ADMIN', 'Administrateur système'),
       ('USER', 'Utilisateur standard'),
       ('MANAGER', 'Gestionnaire'),
       ('GUEST', 'Utilisateur invité');

-- Ajout de permissions
INSERT INTO role_permissions (role_id, permission)
VALUES 
((SELECT id FROM roles WHERE name = 'ADMIN'), 'CREATE_USER'),
((SELECT id FROM roles WHERE name = 'ADMIN'), 'UPDATE_USER'),
((SELECT id FROM roles WHERE name = 'ADMIN'), 'DELETE_USER'),
((SELECT id FROM roles WHERE name = 'ADMIN'), 'VIEW_AUDIT'),
((SELECT id FROM roles WHERE name = 'MANAGER'), 'CREATE_USER'),
((SELECT id FROM roles WHERE name = 'MANAGER'), 'UPDATE_USER'),
((SELECT id FROM roles WHERE name = 'USER'), 'VIEW_PROFILE');

INSERT INTO users (username, email, first_name, last_name, password, role_id)
VALUES ('admin', 'admin@example.com', 'Admin', 'User', 'password123', (SELECT id FROM roles WHERE name = 'ADMIN')),
       ('user1', 'user1@example.com', 'First', 'User', 'password123', (SELECT id FROM roles WHERE name = 'USER')),
       ('user2', 'user2@example.com', 'Second', 'User', 'password123', (SELECT id FROM roles WHERE name = 'USER')),
       ('manager', 'manager@example.com', 'Manager', 'User', 'password123', (SELECT id FROM roles WHERE name = 'MANAGER'));
