DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students_courses CASCADE;

CREATE TABLE groups
(
    group_id   BIGSERIAL PRIMARY KEY,
    group_name VARCHAR(5) NOT NULL UNIQUE
);

CREATE TABLE students
(
    student_id BIGSERIAL PRIMARY KEY,
    group_id   BIGINT,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (group_id)
);

CREATE TABLE courses
(
    course_id          BIGSERIAL PRIMARY KEY,
    course_name        VARCHAR(50)  NOT NULL UNIQUE,
    course_description VARCHAR(255) NOT NULL
);

CREATE TABLE students_courses
(
    id         BIGSERIAL PRIMARY KEY,
    student_id BIGINT,
    course_id  BIGINT,
    CONSTRAINT FK_students FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT FK_courses FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE,
    UNIQUE (student_id, course_id)
);
