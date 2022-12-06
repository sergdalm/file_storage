CREATE TABLE events
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    type    VARCHAR(32) NOT NULL,
    time    TIMESTAMP   NOT NULL,
    user_id INT         NOT NULL,
    file_id INT         NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (file_id) REFERENCES files (id) ON DELETE CASCADE
);