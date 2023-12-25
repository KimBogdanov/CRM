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
    admin_id       INT REFERENCES admin (id)
);
-- rollback DROP TABLE orders;

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
    role       VARCHAR(32)  NOT NULL,
    subject_id INT REFERENCES subject (id),
    avatar     VARCHAR(128)
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
    admin_id    INT REFERENCES admin (id),
    order_id    INT REFERENCES orders (id),
    student_id  INT REFERENCES student (id),
    teacher_id  INT REFERENCES teacher (id)
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
    student_id        INT REFERENCES student (id)
);
-- rollback DROP TABLE abonement;

CREATE TABLE IF NOT EXISTS lesson
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    student_id  INT          NOT NULL REFERENCES student (id),
    teacher_id  INT          NOT NULL REFERENCES teacher (id),
    date_time   TIMESTAMP(0) NOT NULL,
    duration    INT          NOT NULL,
    subject_id  INT          NOT NULL REFERENCES subject (id),
    status      VARCHAR(64)  NOT NULL,
    type        VARCHAR(64),
    description VARCHAR(128),
    cost        NUMERIC(10, 2)
);
--rollback DROP TABLE lesson;

CREATE TABLE IF NOT EXISTS comment
(
    order_id  INT REFERENCES orders (id),
    lesson_id INT REFERENCES lesson (id),
    text      VARCHAR(256),
    added_at  TIMESTAMP(0) NOT NULL,
    PRIMARY KEY (text, added_at)
);
-- rollback DROP TABLE comment;

CREATE TABLE IF NOT EXISTS teachers_subject
(
    teacher_id INT REFERENCES teacher (id) ON DELETE CASCADE,
    subject_id INT REFERENCES subject (id) ON DELETE CASCADE,
    PRIMARY KEY (teacher_id, subject_id)
);
--rollback DROP TABLE teachers_subject;

CREATE TABLE IF NOT EXISTS salary_log
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    payment    NUMERIC(10, 2),
    added_at   TIMESTAMP(0),
    teacher_id INT REFERENCES teacher (id),
    admin_id   INT REFERENCES admin (id)
);
--rollback DROP TABLE salary_log;