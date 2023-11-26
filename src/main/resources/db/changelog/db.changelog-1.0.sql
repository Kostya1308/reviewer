--liquibase formatted sql

--changeset knpiskunov:create_branch_table
CREATE TABLE IF NOT EXISTS branch
(
    id   serial PRIMARY KEY,
    name varchar(50) NOT NULL
);

--changeset knpiskunov:create_branch_sequence
CREATE SEQUENCE branch_is_seq START WITH 1 INCREMENT BY 1;

--changeset knpiskunov:create_launch_line_table
CREATE TABLE IF NOT EXISTS launch_line
(
    id          serial PRIMARY KEY,
    line        varchar(200) NOT NULL,
    result_path varchar(100) NOT NULL,
    branch_id   serial NOT NULL,
    CONSTRAINT fk_launch_line_branch
        FOREIGN KEY (branch_id) REFERENCES branch (id)
);

--changeset knpiskunov:create_command_line_sequence
CREATE SEQUENCE launch_line_is_seq START WITH 1 INCREMENT BY 1;


