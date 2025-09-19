-- First, insert the roles if they don't exist
INSERT INTO roles (name)
SELECT 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (name)
SELECT 'ROLE_USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

INSERT INTO roles (name)
SELECT 'ROLE_CUSTOMER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_CUSTOMER');

-- Insert the admin user with password 'admin@123' (BCrypt encoded)
-- The password hash is for 'admin@123'
INSERT INTO users (user_name, email, password, first_name, last_name, mobile, created_on, modified_on)
SELECT 'YashPatel', 'admin@gmail.com', '$2a$10$IKzwPfMguoXiNV5o177a2urtVh50cnD7Vn5Yv4ozdaxJ7cu6HVHP2',
       'Yash', 'Patel', '8574589658', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@gmail.com');

-- Assign the admin role to the admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.user_id, r.role_id
FROM users u
CROSS JOIN roles r
WHERE u.email = 'admin@gmail.com'
AND r.name = 'ROLE_ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur
    WHERE ur.user_id = u.user_id AND ur.role_id = r.role_id
);