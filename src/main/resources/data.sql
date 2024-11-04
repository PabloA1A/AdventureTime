-- Roles
INSERT INTO roles (role_id, name) VALUES (default, 'ROLE_USER');
INSERT INTO roles (role_id, name) VALUES (default, 'ROLE_ADMIN');

-- Users
-- Users
INSERT INTO users (user_id, username, password) VALUES (default, 'user', '$2a$12$8LegtLQWe717tIPvZeivjuqKnaAs5.bm0Q05.5GrAmcKzXw2NjoUO');
INSERT INTO users (user_id, username, password) VALUES (default, 'admin', '$2a$12$8LegtLQWe717tIPvZeivjuqKnaAs5.bm0Q05.5GrAmcKzXw2NjoUO');

-- Roles_Users 
INSERT INTO roles_users (role_id, user_id) VALUES (1, 1);  
INSERT INTO roles_users (role_id, user_id) VALUES (2, 2);  

-- Profiles
INSERT INTO profiles (email, user_id) VALUES
('user@mail.com', 1),
('admin@mail.com', 2);