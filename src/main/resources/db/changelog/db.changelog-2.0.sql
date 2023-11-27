--liquibase formatted sql

--changeset knpiskunov:fill_branch_table
INSERT INTO branch(name)
VALUES ('feature/core');

INSERT INTO branch(name)
VALUES ('feature/core-read-from-file');

--changeset knpiskunov:fill_launch_line_table
INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100', 'classpath:csv/correct/core/1.csv', 1);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('3-1 2-5 5-1 balanceDebitCard=100', 'classpath:csv/correct/core/2.csv', 1);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('3-1 2-5 5-1 3-1 discountCard=1111 balanceDebitCard=100', 'classpath:csv/correct/core/3.csv', 1);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('3-1 2-5 5-1 3-1 discountCard=1111 balanceDebitCard=0', 'classpath:csv/correct/core/4.csv', 1);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('3-10 2-5 5-1 3-1 balanceDebitCard=100', 'classpath:csv/correct/core/5.csv', 1);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('21-7 2-5 5-1 3-1 balanceDebitCard=100', 'classpath:csv/correct/core/6.csv', 1);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('balanceDebitCard=100', 'classpath:csv/correct/core/7.csv', 1);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('3-7 2-5 5-1 3-1', 'classpath:csv/correct/core/8.csv', 1);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100 pathToFile=./src/main/resources/products.csv saveToFile=1.csv
'', ''classpath:csv/correct/core-read-from-file/1.csv', 'classpath:csv/correct/core.read.from.file/9.csv', 2);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('3-1 2-5 5-1 balanceDebitCard=100 pathToFile=./src/main/resources/products.csv saveToFile=1.csv
'', ''classpath:csv/correct/core-read-from-file/1.csv', 'classpath:csv/correct/core.read.from.file/10.csv', 2);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('3-1 2-5 5-1 3-1 discountCard=1111 balanceDebitCard=100 pathToFile=./src/main/resources/products.csv saveToFile=1.csv
'', ''classpath:csv/correct/core-read-from-file/1.csv', 'classpath:csv/correct/core.read.from.file/11.csv', 2);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('3-1 2-5 5-1 3-1 discountCard=1111 balanceDebitCard=0 pathToFile=./src/main/resources/products.csv saveToFile=1.csv
'', ''classpath:csv/correct/core-read-from-file/1.csv', 'classpath:csv/correct/core.read.from.file/12.csv', 2);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('3-10 2-5 5-1 3-1 balanceDebitCard=100 pathToFile=./src/main/resources/products.csv saveToFile=1.csv
'', ''classpath:csv/correct/core-read-from-file/1.csv', 'classpath:csv/correct/core.read.from.file/13.csv', 2);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('21-7 2-5 5-1 3-1 balanceDebitCard=100 pathToFile=./src/main/resources/products.csv saveToFile=1.csv
'', ''classpath:csv/correct/core-read-from-file/1.csv', 'classpath:csv/correct/core.read.from.file/14.csv', 2);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('balanceDebitCard=100 pathToFile=./src/main/resources/products.csv saveToFile=1.csv
'', ''classpath:csv/correct/core-read-from-file/1.csv', 'classpath:csv/correct/core.read.from.file/15.csv', 2);

INSERT INTO launch_line(arguments, result_path, branch_id)
VALUES ('3-7 2-5 5-1 3-1 pathToFile=./src/main/resources/products.csv saveToFile=1.csv
'', ''classpath:csv/correct/core-read-from-file/1.csv', 'classpath:csv/correct/core.read.from.file/16.csv', 2);
