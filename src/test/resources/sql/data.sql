INSERT INTO ADMIN (ID, FIRST_NAME, LAST_NAME, PHONE, EMAIL, PASSWORD, ROLE, SHIFT_RATE, AVATAR)
VALUES (1, 'Андрей', 'Админов', '8-925-999-99-99', 'adminFirst@gmail.com', '123', 'ADMIN',
        '1900', 'admin_2.jgp'),
       (2, 'Пётр', 'Иванов', '8-925-888-88-88', 'adminSecond@gmail.com', '123', 'ADMIN',
        '1700', 'admin_2.jgp');

INSERT INTO ORDERS (ID, STATUS, ORDER_NAME, CLIENT_NAME, PHONE, REQUEST_SOURCE, CREATED_AT, ADMIN_ID)
VALUES (1, 'UNPROCESSED', 'Глинка/Вокал', 'Илья', '8-924-555-55-55', 'Yandex', '2022-10-10', 1),
       (2, 'APPOINTMENT_SCHEDULED', 'Гитара', 'Андрей', '8-924-555-55-66', 'Авито', '2022-10-10', 1),
       (3, 'POOR_LEAD', 'Скрипка', 'Пётр', '8-924-555-55-77', 'Авито', '2022-10-10', 2),
       (4, 'REFUSED_TO_PURCHASE', 'Фортепиано', 'Маша', '8-924-555-55-88', 'Yandex', '2022-10-10', 1),
       (5, 'UNPROCESSED', 'Ударные', 'Света', '8-924-555-55-99', 'Yandex', '2022-10-10', 2);

INSERT INTO SUBJECT (ID, NAME)
VALUES (1, 'Вокал'),
       (2, 'Гитара'),
       (3, 'Скрипка'),
       (4, 'Ударные'),
       (5, 'Фортепиано');

INSERT INTO STUDENT (ID, FIRST_NAME, LAST_NAME, PHONE, EMAIL, PASSWORD, ROLE, SUBJECT_ID, AVATAR)
VALUES (1, 'Андрей', 'Иванов', '8-925-144-585-52', 'andrey@gmail.com', '123', 'STUDENT', 1, 'andrey.jgp'),
       (2, 'Павел', 'Петров', '8-925-144-585-53', 'pavel@gmail.com', '123', 'STUDENT', 2, 'pavel.jgp'),
       (3, 'Катя', 'Степанова', '8-925-144-585-54', 'katya@gmail.com', '123', 'STUDENT', 3, 'katya.jgp'),
       (4, 'Маша', 'Андреева', '8-925-144-585-55', 'masha@gmail.com', '123', 'STUDENT', 4, 'masha.jgp'),
       (5, 'Егор', 'Афанасьев', '8-925-144-585-56', 'egor@gmail.com', '123', 'STUDENT', 5, 'egor.jgp');

INSERT INTO ABONEMENT(ID, NUMBER_OF_LESSONS, BALANCE, TYPE, BEGIN, EXPIRE, STATUS, STUDENT_ID)
VALUES (1, 4, 4000, 'INDIVIDUAL', '2023-12-10', '2023-12-31', 'ACTIVE', 1),
       (2, 8, 8000, 'INDIVIDUAL', '2023-12-10', '2023-12-31', 'ACTIVE', 2),
       (3, 4, 4000, 'INDIVIDUAL', '2023-12-10', '2023-12-31', 'ACTIVE', 3),
       (4, 12, 12000, 'INDIVIDUAL', '2023-12-10', '2023-12-31', 'ACTIVE', 4),
       (5, 4, 4000, 'INDIVIDUAL', '2023-12-10', '2023-12-31', 'ACTIVE', 5);

INSERT INTO TEACHER(ID, FIRST_NAME, LAST_NAME, PHONE, EMAIL, PASSWORD, ROLE, SALARY_PER_HOUR, STATUS,
                    AVATAR, PAY_RATIO)
VALUES (1, 'Наталья', 'Петрова', '8-88-88-88-8', 'natalya@Gmail.com', '123', 'TEACHER', 900, 'ACTIVE', 'natalya.jpg',
        0.75),
       (2, 'Мария', 'Иванова', '8-88-88-88-9', 'mariay@Gmail.com', '123', 'TEACHER', 800, 'ACTIVE', 'mariya.jpg', 0.7),
       (3, 'Антон', 'Попов', '8-88-88-99-8', 'anton@Gmail.com', '123', 'TEACHER', 1000, 'ACTIVE', 'anton.jpg', 0.8);

INSERT INTO LESSON(ID, STUDENT_ID, TEACHER_ID, DATE_TIME, DURATION, SUBJECT_ID, STATUS, TYPE, DESCRIPTION, COST)
VALUES (1, 1, 1, '2023-12-10', 45, 1, 'APPOINTED', 'FREE', 'description of lesson', 450),
       (2, 2, 2, '2023-12-10', 45, 2, 'APPOINTED', 'PAID', 'description of lesson', 450),
       (3, 3, 3, '2023-12-10', 45, 3, 'APPOINTED', 'PAID', 'description of lesson', 450),
       (4, 4, 1, '2023-12-10', 45, 4, 'APPOINTED', 'PAID', 'description of lesson', 600),
       (5, 5, 2, '2023-12-10', 45, 5, 'APPOINTED', 'PAID', 'description of lesson', 450),
       (6, 1, 3, '2023-12-10', 45, 1, 'APPOINTED', 'PAID', 'description of lesson', 900),
       (7, 2, 1, '2023-12-10', 45, 2, 'APPOINTED', 'PAID', 'description of lesson', 450),
       (8, 3, 2, '2023-12-10', 45, 3, 'APPOINTED', 'PAID', 'description of lesson', 800),
       (9, 4, 3, '2023-12-10', 45, 4, 'APPOINTED', 'PAID', 'description of lesson', 550),
       (10, 5, 1, '2023-12-10', 45, 5, 'APPOINTED', 'PAID', 'description of lesson', 450);

INSERT INTO TEACHERS_SUBJECT (TEACHER_ID, SUBJECT_ID)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (3, 1);