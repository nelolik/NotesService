package ru.nelolik.studingspring.NotesService.db.exception;

public class DataBaseException extends RuntimeException {

    public DataBaseException(Throwable throwable) {
        super(throwable);
    }
}
