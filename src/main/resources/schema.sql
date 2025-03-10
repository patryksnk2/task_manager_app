CREATE TABLE task_attributes (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 category VARCHAR(20) NOT NULL, -- np. 'priority' lub 'status'
                                 name VARCHAR(50) NOT NULL,
                                 UNIQUE (category, name)
);

CREATE TABLE tasks (
                       task_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description VARCHAR(4000),
                       due_date DATE, -- termin
                       task_attributes_id BIGINT,
                       completion_date TIMESTAMP,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (task_attributes_id) REFERENCES task_attributes(id) ON DELETE SET NULL
);

CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE task_assigned_users (
                                     task_id BIGINT NOT NULL,
                                     user_id BIGINT NOT NULL,
                                     PRIMARY KEY (task_id, user_id),
                                     FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
                                     FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
