
DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    is_admin BOOLEAN NOT NULL,
    name VARCHAR(100),
    company VARCHAR(100),
    job_title VARCHAR(100),
    description TEXT
);
DROP TABLE IF EXISTS projects CASCADE;
CREATE TABLE projects (
                          project_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_id BIGINT,
                          name VARCHAR(100) NOT NULL,
                          description TEXT,
                          startDate DATE,
                          deadline DATE,
                          FOREIGN KEY (user_id) REFERENCES users(user_id)
);
DROP TABLE IF EXISTS subprojects CASCADE;
CREATE TABLE subprojects (
                             subproject_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             project_id BIGINT,
                             name VARCHAR(100) NOT NULL,
                             description TEXT,
                             startDate DATE,
                             deadline DATE,
                             FOREIGN KEY (project_id) REFERENCES projects(project_id)
);
DROP TABLE IF EXISTS tasks CASCADE;
CREATE TABLE tasks (
                       task_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       project_id BIGINT,
                       subproject_id BIGINT,
                       name VARCHAR(100) NOT NULL,
                       description TEXT,
                       date DATE,
                       deadline DATE,
                       hours_indicator INT,
                       status ENUM('pending', 'completed') DEFAULT 'pending',
                       FOREIGN KEY (project_id) REFERENCES projects(project_id),
                       FOREIGN KEY (subproject_id) REFERENCES subprojects(subproject_id)
);
INSERT INTO users (user_id,username, password, is_admin, name, company, job_title, description)
VALUES
    (1,'testUser1', 'testPassword1', false, 'Test User 1', 'Test Company 1', 'Test Job Title 1', 'Test Description 1'),
    (2,'testUser2', 'testPassword2', true, 'Test User 2', 'Test Company 2', 'Test Job Title 2', 'Test Description 2');

INSERT INTO projects (user_id, name, description, startDate, deadline)
VALUES
    (1, 'Project 1', 'This is project 1', '2022-01-01', '2022-12-31'),
    (2, 'Project 2', 'This is project 2', '2023-01-01', '2023-12-31');
INSERT INTO subprojects (project_id, name, description, startDate, deadline)
VALUES
    (1, 'Subproject 1', 'This is subproject 1 for project 1', '2022-01-01', '2022-06-30'),
    (1, 'Subproject 2', 'This is subproject 2 for project 1', '2022-07-01', '2022-12-31'),
    (2, 'Subproject 1', 'This is subproject 1 for project 2', '2023-01-01', '2023-06-30');

INSERT INTO tasks (project_id, subproject_id, name, description, date, deadline, hours_indicator, status)
VALUES
    (1, 1, 'Task 1', 'This is task 1 for subproject 1 of project 1', '2022-01-01', '2022-01-31', 10, 'pending'),
    (1, 2, 'Task 2', 'This is task 2 for subproject 2 of project 1', '2022-07-01', '2022-07-31', 20, 'completed'),
    (2, 3, 'Task 1', 'This is task 1 for subproject 1 of project 2', '2023-01-01', '2023-01-31', 30, 'pending');