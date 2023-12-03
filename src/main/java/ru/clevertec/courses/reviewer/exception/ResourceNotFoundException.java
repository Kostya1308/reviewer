package ru.clevertec.courses.reviewer.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "No resource was found in the path %s";

    public ResourceNotFoundException(String path) {
        super(String.format(ERROR_MESSAGE, path));
    }
}
