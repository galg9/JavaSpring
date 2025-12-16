-- Update existing users with BCrypt hashed passwords (password: "password")
-- BCrypt hash for "password"
UPDATE users SET password = '$2a$10$rQcJ0/p8qfJDfGlqQhcPaOvtO3X6V.UmZrRfPQELCvGBVLx8G6gIy'
WHERE username IN ('alice', 'bob');

-- Add an admin user
-- Username: admin, Password: admin (BCrypt hash: $2a$10$X5wFuJBfVKt.L4x.uyBgJOPVpZ7jHDXbP1pGGVQxJ3J9wZZ9ZzXXW)
INSERT INTO users (id, username, password, display_name, role, created_at)
VALUES ('33333333-3333-3333-3333-333333333333', 'admin', '$2a$10$X5wFuJBfVKt.L4x.uyBgJOPVpZ7jHDXbP1pGGVQxJ3J9wZZ9ZzXXW', 'Administrator', 'ADMIN', NOW())
ON CONFLICT (username) DO NOTHING;