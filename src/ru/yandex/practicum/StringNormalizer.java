package ru.yandex.practicum;

public class StringNormalizer {

    private StringNormalizer(){}

    public static String normalizeString(String line){
        return line.toLowerCase().replace('ё', 'е');
    }
}
