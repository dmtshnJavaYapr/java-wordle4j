package ru.yandex.practicum;

public class WordIsNotFoundInDict extends Exception {
    public WordIsNotFoundInDict(String message) {
        super(message);
    }
}
