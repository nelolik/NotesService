package ru.nelolik.studingspring.notes_service.db.exception;

public class DataBaseException extends RuntimeException {

    public DataBaseException(Throwable throwable) {
        super(throwable);
    }
}
