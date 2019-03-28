package ru.otus.bbpax.repository.listner.error;

public class WrongMappingException extends RuntimeException {
    public WrongMappingException(String s) {
        super(s);
    }
}
