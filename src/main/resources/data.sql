INSERT INTO task_attributes (category, name)
VALUES
    ('priority', 'High'),
    ('priority', 'Medium'),
    ('priority', 'Low'),
    ('status', 'Not Started'),
    ('status', 'In Progress'),
    ('status', 'Completed');

INSERT INTO tasks (title, description, due_date, task_attributes_id, completion_date)
VALUES
    ('Finish Project', 'Complete all sections of the project report', '2025-03-20', 1, NULL),
    ('Write Unit Tests', 'Write unit tests for the new feature', '2025-03-15', 2, NULL),
    ('Review Code', 'Review code from team members', '2025-03-12', 3, '2025-03-10 14:30:00');

INSERT INTO users (username, email)
VALUES
    ('patry_ay0lvr8', 'patry_ay0lvr8@example.com'),
    ('john_doe', 'john.doe@example.com'),
    ('jane_smith', 'jane.smith@example.com');

INSERT INTO task_assigned_users (task_id, user_id)
VALUES
    (1, 1), -- Patryk przypisany do zadania "Finish Project"
    (2, 2), -- John przypisany do zadania "Write Unit Tests"
    (3, 3); -- Jane przypisany do zadania "Review Code"

