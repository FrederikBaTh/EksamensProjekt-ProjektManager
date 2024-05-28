CREATE DATABASE IF NOT EXISTS projectmanager_db;

USE projectmanager_db;

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

CREATE TABLE projects (
    project_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    startDate DATE,
    deadline DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


CREATE TABLE project_invitations (
    invitation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT,
    sender_user_id BIGINT,
    receiver_user_id BIGINT,
    status ENUM('pending', 'accepted', 'declined') DEFAULT 'pending',
    FOREIGN KEY (project_id) REFERENCES projects(project_id),
    FOREIGN KEY (sender_user_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_user_id) REFERENCES users(user_id)
);
CREATE TABLE user_project_roles (
    user_project_role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    project_id BIGINT,
    role ENUM('admin', 'viewer', 'invited') DEFAULT 'invited',
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (project_id) REFERENCES projects(project_id)
);
CREATE TABLE subprojects (
    subproject_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    startDate DATE,
    deadline DATE,
    FOREIGN KEY (project_id) REFERENCES projects(project_id)
);
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

CREATE TABLE user_subproject_assignments (
    user_id BIGINT,
    subproject_id BIGINT,
    PRIMARY KEY (user_id, subproject_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (subproject_id) REFERENCES subprojects(subproject_id)
);
CREATE TABLE experiences (
    experience_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    skill VARCHAR(255) NOT NULL,
    years_of_experience INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
