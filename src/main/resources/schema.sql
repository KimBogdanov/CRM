CREATE TABLE IF NOT EXISTS admin
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(128)  NOT NULL,
    last_name  VARCHAR(128)  NOT NULL,
    phone      VARCHAR(32)   NOT NULL UNIQUE,
    email      VARCHAR(128)  NOT NULL UNIQUE,
    password   VARCHAR(64)   NOT NULL,
    role       VARCHAR(32)   NOT NULL,
    shift_rate NUMERIC(8, 2) NOT NULL,
    avatar     VARCHAR(128)
    );
-- rollback DROP TABLE admin

CREATE TABLE IF NOT EXISTS orders
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    status         VARCHAR(64)  NOT NULL,
    order_name     VARCHAR(64),
    client_name    VARCHAR(64),
    phone          VARCHAR(32)  NOT NULL,
    request_source VARCHAR(512),
    created_at     TIMESTAMP(0) NOT NULL,
    admin_id       INT
    );
-- rollback DROP TABLE orders;

CREATE TABLE IF NOT EXISTS comment
(
    order_id  INT,
    lesson_id INT,
    text      VARCHAR(256),
    added_at  TIMESTAMP(0) NOT NULL,
    PRIMARY KEY (text, added_at)
    );
-- rollback DROP TABLE comment;

CREATE TABLE IF NOT EXISTS subject
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
    );
--rollback DROP TABLE log_info;

CREATE TABLE IF NOT EXISTS student
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(128) NOT NULL,
    last_name  VARCHAR(128),
    phone      VARCHAR(32)  NOT NULL,
    email      VARCHAR(128) UNIQUE,
    password   VARCHAR(64),
    avatar     VARCHAR(128),
    role       VARCHAR(32)  NOT NULL,
    subject_id INT
    );
-- rollback DROP TABLE subject;

CREATE TABLE IF NOT EXISTS teacher
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    first_name      VARCHAR(128) NOT NULL,
    last_name       VARCHAR(128) NOT NULL,
    phone           VARCHAR(32)  NOT NULL,
    email           VARCHAR(128) NOT NULL,
    password        VARCHAR(64)  NOT NULL,
    role            VARCHAR(32)  NOT NULL,
    avatar          VARCHAR(128),
    salary_per_hour NUMERIC(10, 2),
    subject_id      INT,
    status          VARCHAR(32)  NOT NULL,
    pay_ratio       DOUBLE
    );
-- rollback DROP TABLE teacher;

CREATE TABLE IF NOT EXISTS log_info
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    action_type VARCHAR(256),
    description VARCHAR(256),
    created_at  TIMESTAMP(0) NOT NULL,
    admin_id    INT,
    order_id    INT,
    student_id  INT,
    teacher_id  INT
    );
-- rollback DROP TABLE student;

CREATE TABLE IF NOT EXISTS abonement
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    number_of_lessons INT     NOT NULL,
    balance           NUMERIC(10, 2),
    type              VARCHAR(32),
    begin             DATE,
    expire            DATE,
    status            VARCHAR NOT NULL,
    student_id        INT
    );
-- rollback DROP TABLE abonement;

CREATE TABLE IF NOT EXISTS lesson
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    student_id  INT          NOT NULL,
    teacher_id  INT          NOT NULL,
    date_time   TIMESTAMP(0) NOT NULL,
    duration    INT          NOT NULL,
    subject_id  INT          NOT NULL,
    status      VARCHAR(64)  NOT NULL,
    type        VARCHAR(64),
    description VARCHAR(128),
    cost        NUMERIC(10, 2)
    );
--rollback DROP TABLE lesson;

CREATE TABLE IF NOT EXISTS teachers_subject
(
    teacher_id INT,
    subject_id INT,
    PRIMARY KEY (teacher_id, subject_id)
    );
--rollback DROP TABLE teachers_subject;

CREATE TABLE IF NOT EXISTS salary_log
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    payment    NUMERIC(10, 2),
    added_at   TIMESTAMP(0),
    teacher_id INT,
    admin_id   INT
    );
--rollback DROP TABLE salary_log;

-- Добавляем внешние ключи

ALTER TABLE orders ADD CONSTRAINT fk_orders_admin FOREIGN KEY (admin_id) REFERENCES admin (id);

ALTER TABLE comment ADD CONSTRAINT fk_comment_order FOREIGN KEY (order_id) REFERENCES orders (id);
ALTER TABLE comment ADD CONSTRAINT fk_comment_lesson FOREIGN KEY (lesson_id) REFERENCES lesson (id);

ALTER TABLE student ADD CONSTRAINT fk_student_subject FOREIGN KEY (subject_id) REFERENCES subject (id);

ALTER TABLE teacher ADD CONSTRAINT fk_teacher_subject FOREIGN KEY (subject_id) REFERENCES subject (id);

ALTER TABLE log_info ADD CONSTRAINT fk_log_info_admin FOREIGN KEY (admin_id) REFERENCES admin (id);
ALTER TABLE log_info ADD CONSTRAINT fk_log_info_order FOREIGN KEY (order_id) REFERENCES orders (id);
ALTER TABLE log_info ADD CONSTRAINT fk_log_info_student FOREIGN KEY (student_id) REFERENCES student (id);
ALTER TABLE log_info ADD CONSTRAINT fk_log_info_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id);

ALTER TABLE abonement ADD CONSTRAINT fk_abonement_student FOREIGN KEY (student_id) REFERENCES student (id);

ALTER TABLE lesson ADD CONSTRAINT fk_lesson_student FOREIGN KEY (student_id) REFERENCES student (id);
ALTER TABLE lesson ADD CONSTRAINT fk_lesson_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id);
ALTER TABLE lesson ADD CONSTRAINT fk_lesson_subject FOREIGN KEY (subject_id) REFERENCES subject (id);

ALTER TABLE teachers_subject ADD CONSTRAINT fk_teachers_subject_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id);
ALTER TABLE teachers_subject ADD CONSTRAINT fk_teachers_subject_subject FOREIGN KEY (subject_id) REFERENCES subject (id);

ALTER TABLE salary_log ADD CONSTRAINT fk_salary_log_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id);
ALTER TABLE salary_log ADD CONSTRAINT fk_salary_log_admin FOREIGN KEY (admin_id) REFERENCES admin (id);
