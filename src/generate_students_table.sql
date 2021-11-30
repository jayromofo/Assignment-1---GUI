create table assignment.students
(
    student_id text not null
        constraint pk_student_id
            primary key,
    first_name text not null,
    last_name  text not null,
    program    text not null
);

alter table assignment.students
    owner to postgres;


