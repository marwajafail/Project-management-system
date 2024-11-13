-- Seed Roles
INSERT INTO role (roleId, name, description) VALUES
(1, 'Manager', 'User who manages projects.'),
(2, 'Member', 'User who is a member of the project.');

-- Seed Profiles
INSERT INTO profile (profileId, firstName, lastName, profilePic) VALUES
(1, 'Marwa', 'Jafail', 'upload/pic2.png');

-- Seed Users
INSERT INTO Users (uid, email, password, status, profile_id, role_id, is_enabled, reset_token, token_expiration_time, verification_code) VALUES
(1, 'marwa.a.jafail@gmail.com', '$2a$10$pqS8QfbIBFtNS9mHbY8KcOkQ/htsvH8FXw3EGH.dtZB7ClyQOXiIG', true, 1, 1, true, NULL, NULL, NULL),
(2, 'marwaalihj@gmail.com', '$2a$10$pqS8QfbIBFtNS9mHbY8KcOkQ/htsvH8FXw3EGH.dtZB7ClyQOXiIG', true, 2, 2, true, NULL, NULL, NULL);

-- Seed Types (Using "Development" and "Marketing")
INSERT INTO type (typeId, name, description, uid) VALUES
(1, 'Development', 'Projects focused on software or product development.', 1),
(2, 'Marketing', 'Projects related to marketing campaigns and promotions.', 1);

-- Seed Projects
INSERT INTO project (projectId, name, description, startDate, endDate, typeId, uid) VALUES
(1, 'Website Redesign', 'Redesigning the company website to improve user experience.', NOW(), NOW() + INTERVAL '30 days', 1, 1),
(2, 'Mobile App Launch Campaign', 'Marketing campaign for the launch of the mobile app.', NOW(), NOW() + INTERVAL '30 days', 2, 1);

-- Seed Tasks
INSERT INTO task (taskId, title, description, isCompleted, projectId, assigned_to) VALUES
(1, 'Create Wireframes', 'Design wireframes for the new website.', false, 1, 2),
(2, 'Develop Frontend', 'Implement the frontend design using HTML, CSS, and JavaScript.', false, 1, 2),
(3, 'Build API', 'Create the backend API for the mobile app.', false, 1, 2),
(4, 'Social Media Promotions', 'Plan and execute social media strategies for app launch.', false, 2, 2),
(5, 'Email Campaign', 'Create and send email campaigns to announce app launch.', false, 2, 2);
