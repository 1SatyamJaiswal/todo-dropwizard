CREATE TABLE IF NOT EXISTS todolist (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  startDateTime DATETIME NOT NULL,
  targetDateTime DATETIME NOT NULL,
  status ENUM('Todo', 'WIP', 'Done') NOT NULL
);
