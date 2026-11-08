package ru.yandex.practicum;

public class WordIsNotFoundInDict extends RuntimeException {
    public WordIsNotFoundInDict(String message) {
        super(message);
    }
}
