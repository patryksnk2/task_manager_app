-- Usuwamy najpierw tabele zależne, potem te nadrzędne
DROP TABLE IF EXISTS task_comments;
DROP TABLE IF EXISTS task_assigned_users;
DROP TABLE IF EXISTS task_tags;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS task_attributes;

-- Tworzymy tabele w odpowiedniej kolejności:

-- 1. Tabela task_attributes (brak zależności)
CREATE TABLE task_attributes (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 category VARCHAR(20) NOT NULL, -- np. 'priority' lub 'status'
                                 name VARCHAR(50) NOT NULL,
                                 UNIQUE (category, name)
);

-- 2. Tabela tasks (odnosi się do task_attributes oraz sama siebie)
CREATE TABLE tasks (
                       task_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description VARCHAR(4000),
                       due_date DATE,
                       status_id BIGINT,
                       priority_id BIGINT,
                       parent_task_id BIGINT,
                       completion_date TIMESTAMP,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (status_id) REFERENCES task_attributes(id) ON DELETE SET NULL,
                       FOREIGN KEY (priority_id) REFERENCES task_attributes(id) ON DELETE SET NULL,
                       FOREIGN KEY (parent_task_id) REFERENCES tasks(task_id) ON DELETE SET NULL
);

-- 3. Tabela tags (niezależna)
CREATE TABLE tags (
                      tag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE
);

-- 4. Tabela task_tags (relacja many-to-many między tasks a tags)
CREATE TABLE task_tags (
                           task_id BIGINT NOT NULL,
                           tag_id BIGINT NOT NULL,
                           PRIMARY KEY (task_id, tag_id),
                           FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
                           FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE
);

-- 5. Tabela users (niezależna)
CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

-- 6. Tabela task_assigned_users (relacja many-to-many między tasks a users)
CREATE TABLE task_assigned_users (
                                     task_id BIGINT NOT NULL,
                                     user_id BIGINT NOT NULL,
                                     PRIMARY KEY (task_id, user_id),
                                     FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
                                     FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 7. Tabela task_comments (relacja one-to-many: jedno zadanie -> wiele komentarzy, każdy komentarz powiązany z użytkownikiem)
CREATE TABLE task_comments (
                               comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               task_id BIGINT NOT NULL,
                               user_id BIGINT NOT NULL,
                               content VARCHAR(4000) NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
                               FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
