package ru.otus.bbpax.service.error;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String objectName, Long id) {
        this(objectName + "with id '" + id + "' was not found");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
