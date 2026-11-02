package ru.yandex.practicum;

import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) throws WordsLengthIsNotFive, WordIsNotFoundInDict, WordsLengthIsNotFive,
            EnglishWordException {
        // try {
            WordleDictionary wordleDictionary = new WordleDictionary(WordleDictionaryLoader.loadToList());
            WordleGame wordleGame = new WordleGame(wordleDictionary);
            Scanner scanner = new Scanner(System.in);

            while (wordleGame.getSteps() != 0) {
                String userWord = scanner.nextLine();

                if (userWord.isBlank()) {
                    userWord = wordleGame.getWordFromAvailable();
                    System.out.println(userWord);
                }

                try {
                    wordleDictionary.checkIsWordNotRussian(userWord);
                } catch (EnglishWordException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                try {
                    wordleDictionary.checkUserWordInDict(userWord);
                } catch (WordIsNotFoundInDict e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                try {
                    wordleDictionary.checkWordRules(userWord);
                } catch (WordsLengthIsNotFive e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                wordleGame.fillClueKeeper(userWord);
                String clue = wordleDictionary.getCharsClue(wordleGame.getAnswer(), userWord);
                System.out.println(clue);
                wordleGame.updateAvailableWords();
                wordleGame.nextStep();

                if (userWord.equals(wordleGame.getAnswer()))
                    break;
            }
        // }
    }

}
