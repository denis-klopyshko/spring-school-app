INSERT INTO groups (group_id, group_name)
VALUES (100, 'GR-10'),
       (101, 'GR-11'),
       (102, 'GR-12'),
       (103, 'GR-13');

INSERT INTO courses (course_id, course_name, course_description)
VALUES (100, 'Math', 'Math Description'),
       (101, 'Biology', 'Biology Description'),
       (102, 'Programming', 'Programming Description');

INSERT INTO students (student_id, group_id, first_name, last_name)
VALUES (100, 100, 'John', 'Snow'),
       (101, 101, 'Bob', 'Rogers'),
       (102, 102, 'Roger', 'That');
