INSERT INTO ADMIN (ID, FIRST_NAME, LAST_NAME, PHONE, EMAIL, PASSWORD, ROLE, SHIFT_RATE, AVATAR)
VALUES (1, 'Андрей', 'Админов', '8-925-999-99-99', 'adminFirst@gmail.com', '123', 'ADMIN',
        '1900', 'admin_2.jgp'),
       (2, 'Пётр', 'Иванов', '8-925-888-88-88', 'adminSecond@gmail.com', '123', 'ADMIN',
        '1700', 'admin_2.jgp');

INSERT INTO ORDERS (ID, STATUS, ORDER_NAME, CLIENT_NAME, PHONE, REQUEST_SOURCE, CREATED_AT, ADMIN_ID)
VALUES (7, 'UNPROCESSED', 'Глинка/Вокал', 'Илья', '8-924-555-55-55', 'Yandex', '2022-10-10', 1);

INSERT INTO SUBJECT (ID, NAME)
VALUES (1, 'Вокал');

INSERT INTO STUDENT (ID, FIRST_NAME, LAST_NAME, PHONE, EMAIL, PASSWORD, ROLE, SUBJECT_ID, AVATAR)
VALUES (1, 'Андрей', 'Иванов', '8-5-5-65', 'andrey@gmail.com', '123', 'STUDENT', 1, 'student.jgp');

INSERT INTO ABONEMENT_TYPE (ID, NAME)
VALUES (1, 'Group');

INSERT INTO ABONEMENT(ID, NUMBER_OF_LESSONS, BALANCE, TYPE_ID, BEGIN, EXPIRE, STATUS, STUDENT_ID)
VALUES (1, 4, 1500, 1, '2023-12-10', '2023-12-31', 'ACTIVE', 1),
       (2, 4, 1700, 1, '2023-12-10', '2023-12-31', 'ACTIVE', 1),
       (3, 4, 1700, 1, '2023-12-10', '2023-12-31', 'ACTIVE', 1);