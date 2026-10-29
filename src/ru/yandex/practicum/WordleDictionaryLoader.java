package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    public static List<String> loadToList(){
        try (BufferedReader br = new BufferedReader(new FileReader("words_ru.txt",
                StandardCharsets.UTF_8))) {
            List<String> list = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null){
                if (line.length() == 5) {
                    line = StringNormalizer.normalizeString(line);
                    list.add(line);
                }
            }
            return list;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Указанный файл не существует", e);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении словаря", e);
        }
    }
}
