package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    private PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public List<String> loadToList(String fileName) throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName,
                StandardCharsets.UTF_8))) {
            log.println("Начинаем загрузку словаря");
            List<String> list = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null ){
                if (line.length() == 5) {
                    line = StringNormalizer.normalizeString(line);
                    list.add(line);
                }
            }

            log.println("Загрузили словарь");
            return list;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении словаря", e);
        }
    }
}
