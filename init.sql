CREATE DATABASE customer;
CREATE DATABASE notification;
CREATE DATABASE fraud;
CREATE DATABASE auth;

GRANT ALL PRIVILEGES ON DATABASE customer TO usuario;
GRANT ALL PRIVILEGES ON DATABASE notification TO usuario;
GRANT ALL PRIVILEGES ON DATABASE fraud TO usuario;
GRANT ALL PRIVILEGES ON DATABASE auth TO usuario;

-- Initialize auth database schema
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

-- Grant permissions to usuario
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO usuario;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO usuario;

-- Insert initial data
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

INSERT INTO roles (id, name, description)
VALUES (1, 'ROLE_USER', 'Standard user role'),
       (2, 'ROLE_ADMIN', 'Administrator role'),
       (3, 'ROLE_MANAGER', 'Manager role'),
       (4, 'ROLE_OPERATOR', 'Operator role')
ON CONFLICT (id) DO NOTHING;

-- Assign Permissions to Roles
INSERT INTO role_permissions (role_id, permission_id)
VALUES (1, 1),
       (1, 4),
       (1, 6),
       (4, 1),
       (4, 2),
       (4, 4),
       (4, 5),
       (4, 6),
       (4, 7),
       (3, 1),
       (3, 2),
       (3, 3),
       (3, 4),
       (3, 5),
       (3, 6),
       (3, 7),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 7),
       (2, 8)
ON CONFLICT DO NOTHING;

-- Insert test users (password: 'password123')
INSERT INTO users (id, username, email, password, enabled, account_non_expired, account_non_locked,
                   credentials_non_expired, created_at, updated_at)
VALUES (1, 'admin', 'admin@microservices.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/1LkdNhPFNqkPYk7NHw2hHJ7wDzVe9O', true,
        true, true, true, NOW(), NOW()),
       (2, 'manager', 'manager@microservices.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/1LkdNhPFNqkPYk7NHw2hHJ7wDzVe9O', true,
        true, true, true, NOW(), NOW()),
       (3, 'operator', 'operator@microservices.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/1LkdNhPFNqkPYk7NHw2hHJ7wDzVe9O',
        true, true, true, true, NOW(), NOW()),
       (4, 'user', 'user@microservices.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye/1LkdNhPFNqkPYk7NHw2hHJ7wDzVe9O', true, true,
        true, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 2),
       (2, 3),
       (3, 4),
       (4, 1)
ON CONFLICT DO NOTHING;

-- Update sequences
SELECT setval('users_id_seq', (SELECT COALESCE(MAX(id), 0) FROM users) + 1, false);
SELECT setval('roles_id_seq', (SELECT COALESCE(MAX(id), 0) FROM roles) + 1, false);
SELECT setval('permissions_id_seq', (SELECT COALESCE(MAX(id), 0) FROM permissions) + 1, false);

