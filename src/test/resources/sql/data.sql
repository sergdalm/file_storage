INSERT INTO users(id, username, email, password, role)
VALUES (1, 'sergdalm', 'alex@gmail.com', '24r3u', 'ADMIN'),
       (2, 'Dimetros', 'dmitry@gmail.com', '18393', 'MODERATOR'),
       (3, 'luiza', 'luiza@gmail.com', '23f3f', 'MODERATOR'),
       (4, 'lotos', 'svetlana@gmail.com', '33233', 'USER'),
       (5, 'thom', 'thomyorke@gmail.com', '55t4g', 'USER'),
       (6, 'seonyoung', 'cheseonyoung@gmail.com', 'r23f4', 'USER');

INSERT INTO files(id, name, size, file_url)
VALUES (1, 'calendar.xls', 10000, 'https://s3.us-east-2.amazonaws.com/bucket/Sun_image2022-12-06T11:43:12.712'),
       (2, 'cats.png', 30000, 'https://s3.us-east-2.amazonaws.com/bucket/Moon_image2022-15-06T11:30:15.600'),
       (3, 'wolf.jpg', 40000, 'https://s3.us-east-2.amazonaws.com/bucket/Earth_image2022-12-15T11:40:10.573');

INSERT INTO events(id, type, time, user_id, file_id)
VALUES (1, 'UPLOAD', '2022-11-22 13:04:48', 1, 1),
       (2, 'UPLOAD', '2022-11-22 13:05:50', 2, 2),
       (3, 'UPLOAD', '2022-11-22 13:05:50', 3, 3),
       (4, 'DOWNLOAD', '2022-11-22 13:05:50', 4, 1),
       (5, 'DOWNLOAD', '2022-11-22 13:05:50', 4, 2),
       (6, 'DOWNLOAD', '2022-11-22 13:05:50', 5, 2),
       (7, 'DOWNLOAD', '2022-11-22 13:05:50', 6, 2),
       (8, 'DOWNLOAD', '2022-11-22 13:05:50', 6, 3);

