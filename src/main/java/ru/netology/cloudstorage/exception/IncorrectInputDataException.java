package ru.netology.cloudstorage.exception;

public class IncorrectInputDataException extends RuntimeException {
    public IncorrectInputDataException(String msg) {
        super(msg);
    }
}
