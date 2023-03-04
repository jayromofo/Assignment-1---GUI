create table assignment.student_marks
(
    mark_1 numeric,
    mark_2 numeric,
    mark_3 numeric,
    mark_4 numeric,
    mark_5 numeric,
    mark_6 numeric,
    id     text
        constraint fk_student_id
            references assignment.students
);

alter table assignment.student_marks
    owner to postgres;


