-- Add role column to users table
ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';

-- Update existing users with appropriate roles
-- alice and bob get USER role by default (already set by DEFAULT)
-- You can manually assign ADMIN role to specific users later