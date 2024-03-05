package ru.yandex.practicum.filmorate.exceptions;

public class AlreadyExistException extends Throwable{
    public AlreadyExistException(String message) {
        super(message);
    }
}
