package ru.clevertec.courses.reviewer.exception;

public class DatabaseFillingException extends RuntimeException {

    public DatabaseFillingException(String path, String table) {
        super(String.format("%s. Таблица '%s' не заполнена", path, table));
    }
}
