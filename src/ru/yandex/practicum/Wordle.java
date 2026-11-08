package ru.yandex.practicum;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {
    public static void main(String[] args) throws FileNotFoundException, EnglishWordException, WordIsNotFoundInDict,
            WordsLengthIsNotFive {

        try (PrintWriter log = new PrintWriter("wordle.log")) {

            log.println("Программа запущена");

            try {
                WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
                WordleDictionary wordleDictionary =
                        new WordleDictionary(loader.loadToList("words_ru.txt"), log);

                WordleGame wordleGame = new WordleGame(wordleDictionary, log);
                log.println("\nПравильный ответ: " + wordleGame.getAnswer() + "\n");
                Scanner scanner = new Scanner(System.in);

                while (wordleGame.getSteps() != 0) {
                    String userWord = scanner.nextLine();

                    if (userWord.isBlank()) {
                        userWord = wordleGame.getWordFromAvailable();
                        System.out.println(userWord);
                    }

                    try {
                        wordleDictionary.checkIsWordNotRussian(userWord);
                        wordleDictionary.checkUserWordInDict(userWord);
                        wordleDictionary.checkWordRules(userWord);
                    } catch (EnglishWordException |
                             WordIsNotFoundInDict |
                             WordsLengthIsNotFive e) {

                        System.out.println(e.getMessage());
                        continue;
                    }

                    wordleGame.fillClueKeeper(userWord);

                    String clue = wordleDictionary.getCharsClue(
                            wordleGame.getAnswer(), userWord);

                    System.out.println(clue);

                    wordleGame.updateAvailableWords();
                    log.println("Список доступных слов: " + wordleGame.getSizeAvailableWords());
                    log.println(wordleGame.getAvailableWords());
                    wordleGame.nextStep();

                    if (userWord.equals(wordleGame.getAnswer())) {
                        break;
                    }
                }
                System.out.println("Загаданное слово: " + wordleGame.getAnswer());

            } catch (FileNotFoundException e) {
                log.println("Не нашли файл словаря!");
            } catch (Exception e) {
                System.err.println("Случился некоторый пиздец, смотри лог");
                log.println("Произошла системная ошибка:");
                e.printStackTrace(log);
            }

            log.println("Программа завершена");

        } catch (FileNotFoundException e) {
            System.err.println("Не удалось создать лог-файл: " + e.getMessage());
        }


    }
}