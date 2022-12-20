INSERT INTO users(id, username, email, password, role)
VALUES (1, 'sergdalm', 'alex@gmail.com', '{noop}24r3u', 'ADMIN'),
       (2, 'dimetros', 'dmitry@gmail.com', '{noop}18393', 'MODERATOR'),
       (3, 'luiza', 'luiza@gmail.com', '{noop}23f3f', 'USER');

INSERT INTO files(id, name, size, file_url)
VALUES (1, 'text', 5076, 'text');

INSERT INTO events(id, type, time, user_id, file_id)
VALUES (1, 'UPLOAD', '2022-11-22T13:04:48', 1, 1),
       (2, 'DOWNLOAD', '2022-11-22T13:05:50', 3, 1);

