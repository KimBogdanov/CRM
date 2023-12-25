INSERT INTO ADMIN (ID, FIRST_NAME, LAST_NAME, PHONE, EMAIL, PASSWORD, ROLE, SHIFT_RATE, AVATAR)
VALUES (1, 'Андрей', 'Админов', '8-925-999-99-99', 'adminFirst@gmail.com', '123', 'ADMIN',
        '1900', 'admin_2.jgp'),
       (2, 'Пётр', 'Иванов', '8-925-888-88-88', 'adminSecond@gmail.com', '123', 'ADMIN',
        '1700', 'admin_2.jgp');

INSERT INTO ORDERS (ID, STATUS, ORDER_NAME, CLIENT_NAME, PHONE, REQUEST_SOURCE, CREATED_AT, ADMIN_ID)
VALUES (7, 'UNPROCESSED', 'Глинка/Вокал', 'Илья', '8-924-555-55-55', 'Yandex', '2022-10-10', 1);

INSERT INTO SUBJECT (ID, NAME)
VALUES (1, 'Вокал'),
       (2, 'Гитара');

INSERT INTO STUDENT (ID, FIRST_NAME, LAST_NAME, PHONE, EMAIL, PASSWORD, ROLE, SUBJECT_ID, AVATAR)
VALUES (1, 'Андрей', 'Иванов', '8-5-5-65', 'andrey@gmail.com', '123', 'STUDENT', 1, 'student.jgp');

INSERT INTO ABONEMENT(ID, NUMBER_OF_LESSONS, BALANCE, TYPE, BEGIN, EXPIRE, STATUS, STUDENT_ID)
VALUES (1, 4, 1500, 'INDIVIDUAL', '2023-12-10', '2023-12-31', 'ACTIVE', 1);

INSERT INTO TEACHER(ID, FIRST_NAME, LAST_NAME, PHONE, EMAIL, PASSWORD, ROLE, SALARY_PER_HOUR, STATUS,
                    AVATAR, PAY_RATIO)
VALUES (1, 'Наталья', 'Петрова', '8-88-88-88-8', 'natalya@Gmail.com', '123', 'TEACHER', 500, 'ACTIVE', 'teacher.jpg',
        0.75);

INSERT INTO LESSON(ID, STUDENT_ID, TEACHER_ID, DATE_TIME, DURATION, SUBJECT_ID, STATUS, TYPE, DESCRIPTION, COST)
VALUES (1, 1, 1, '2023-12-10', 45, 1, 'APPOINTED', 'PAID', 'description of lesson', 450)
