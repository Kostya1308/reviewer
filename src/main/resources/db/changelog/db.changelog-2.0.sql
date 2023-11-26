--liquibase formatted sql

--changeset knpiskunov:fill_branch_table
INSERT INTO branch(name)
VALUES ('feature/core');

INSERT INTO branch(name)
VALUES ('feature/core-read-from-file');

--changeset knpiskunov:fill_launch_line_table
INSERT INTO launch_line(line, result_path, branch_id)
VALUES ('3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100', 'classpath:csv/correct/1.csv', 1);

INSERT INTO launch_line(line, result_path, branch_id)
VALUES ('3-1 2-5 5-1 balanceDebitCard=100', 'classpath:csv/correct/2.csv', 1);


