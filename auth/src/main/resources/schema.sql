-- Auth Service Database Schema
-- Run this after creating the auth database

\c auth;

-- Create sequences
CREATE SEQUENCE IF NOT EXISTS users_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS roles_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS permissions_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS refresh_tokens_id_seq START WITH 1 INCREMENT BY 1;

-- Create users table
CREATE TABLE IF NOT EXISTS users
(
    id                      BIGINT PRIMARY KEY    DEFAULT nextval('users_id_seq'),
    username                VARCHAR(50)  NOT NULL UNIQUE,
    email                   VARCHAR(100) NOT NULL UNIQUE,
    password                VARCHAR(255) NOT NULL,
    enabled                 BOOLEAN      NOT NULL DEFAULT true,
    account_non_expired     BOOLEAN               DEFAULT true,
    account_non_locked      BOOLEAN               DEFAULT true,
    credentials_non_expired BOOLEAN               DEFAULT true,
    created_at              TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Create roles table
CREATE TABLE IF NOT EXISTS roles
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('roles_id_seq'),
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200)
);

-- Create permissions table
CREATE TABLE IF NOT EXISTS permissions
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('permissions_id_seq'),
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(200)
);

-- Create user_roles junction table
CREATE TABLE IF NOT EXISTS user_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- Create role_permissions junction table
CREATE TABLE IF NOT EXISTS role_permissions
(
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE CASCADE
);

-- Create refresh_tokens table
CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id          BIGINT PRIMARY KEY    DEFAULT nextval('refresh_tokens_id_seq'),
    user_id     BIGINT       NOT NULL,
    token       VARCHAR(500) NOT NULL UNIQUE,
    expiry_date TIMESTAMP    NOT NULL,
    revoked     BOOLEAN      NOT NULL DEFAULT false,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users (username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_token ON refresh_tokens (token);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user_id ON refresh_tokens (user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_expiry ON refresh_tokens (expiry_date);
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON user_roles (user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role_id ON user_roles (role_id);
CREATE INDEX IF NOT EXISTS idx_role_permissions_role_id ON role_permissions (role_id);
CREATE INDEX IF NOT EXISTS idx_role_permissions_permission_id ON role_permissions (permission_id);

-- Insert Permissions
INSERT INTO permissions (id, name, description)
VALUES (1, 'customer:read', 'Read customer data'),
       (2, 'customer:write', 'Create and update customer data'),
       (3, 'customer:delete', 'Delete customer data'),
       (4, 'fraud:read', 'Read fraud check data'),
       (5, 'fraud:write', 'Create fraud check records'),
       (6, 'notification:read', 'Read notification data'),
       (7, 'notification:write', 'Send notifications'),
       (8, 'admin:all', 'Full administrative access')
ON CONFLICT (id) DO NOTHING;

-- Insert Roles
INSERT INTO roles (id, name, description)
VALUES (1, 'ROLE_USER', 'Standard user role'),
       (2, 'ROLE_ADMIN', 'Administrator role'),
       (3, 'ROLE_MANAGER', 'Manager role'),
       (4, 'ROLE_OPERATOR', 'Operator role')
ON CONFLICT (id) DO NOTHING;

-- Assign Permissions to Roles
-- ROLE_USER permissions
INSERT INTO role_permissions (role_id, permission_id)
VALUES (1, 1), -- customer:read
       (1, 4), -- fraud:read
       (1, 6)  -- notification:read
ON CONFLICT DO NOTHING;

-- ROLE_OPERATOR permissions
INSERT INTO role_permissions (role_id, permission_id)
VALUES (4, 1), -- customer:read
       (4, 2), -- customer:write
       (4, 4), -- fraud:read
       (4, 5), -- fraud:write
       (4, 6), -- notification:read
       (4, 7)  -- notification:write
ON CONFLICT DO NOTHING;

-- ROLE_MANAGER permissions
INSERT INTO role_permissions (role_id, permission_id)
VALUES (3, 1), -- customer:read
       (3, 2), -- customer:write
       (3, 3), -- customer:delete
       (3, 4), -- fraud:read
       (3, 5), -- fraud:write
       (3, 6), -- notification:read
       (3, 7)  -- notification:write
ON CONFLICT DO NOTHING;

-- ROLE_ADMIN permissions (all)
INSERT INTO role_permissions (role_id, permission_id)
VALUES (2, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 7),
       (2, 8)
ON CONFLICT DO NOTHING;

-- Insert test users (password is 'password123' for all)
-- Password hash: $2a$10$8dzSwZskilOe8D9UJUKkpunuLEJgByaIZvg7vBB50GlU5d5BusMUe
INSERT INTO users (id, username, email, password, enabled, account_non_expired, account_non_locked,
                   credentials_non_expired, created_at, updated_at)
VALUES (1, 'admin', 'admin@microservices.com', '$2a$10$8dzSwZskilOe8D9UJUKkpunuLEJgByaIZvg7vBB50GlU5d5BusMUe', true,
        true, true, true, NOW(), NOW()),
       (2, 'manager', 'manager@microservices.com', '$2a$10$8dzSwZskilOe8D9UJUKkpunuLEJgByaIZvg7vBB50GlU5d5BusMUe', true,
        true, true, true, NOW(), NOW()),
       (3, 'operator', 'operator@microservices.com', '$2a$10$8dzSwZskilOe8D9UJUKkpunuLEJgByaIZvg7vBB50GlU5d5BusMUe',
        true, true, true, true, NOW(), NOW()),
       (4, 'user', 'user@microservices.com', '$2a$10$8dzSwZskilOe8D9UJUKkpunuLEJgByaIZvg7vBB50GlU5d5BusMUe', true, true,
        true, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Assign Roles to Users
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 2), -- admin -> ROLE_ADMIN
       (2, 3), -- manager -> ROLE_MANAGER
       (3, 4), -- operator -> ROLE_OPERATOR
       (4, 1)  -- user -> ROLE_USER
ON CONFLICT DO NOTHING;

-- Update sequences to correct values
SELECT setval('users_id_seq', (SELECT COALESCE(MAX(id), 0) FROM users) + 1, false);
SELECT setval('roles_id_seq', (SELECT COALESCE(MAX(id), 0) FROM roles) + 1, false);
SELECT setval('permissions_id_seq', (SELECT COALESCE(MAX(id), 0) FROM permissions) + 1, false);

-- Grant permissions to usuario
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO usuario;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO usuario;

