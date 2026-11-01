package ru.yandex.practicum;

import java.util.*;

import static java.awt.SystemColor.text;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;
    private int steps = 6;
    private WordleDictionary dictionary;
    private ClueKeeper clueKeeper;

    private List<String> userWords = new ArrayList<>();
    private List<String> availableWords;
    Random rand = new Random();

    public WordleGame(WordleDictionary wordleDictionary) {
        this.dictionary = wordleDictionary;
        answer = dictionary.getRandomWord();
        availableWords = new ArrayList<>(dictionary.getWords());
        clueKeeper = new ClueKeeper();
    }

    private void fillClueKeeper(String userWord) {
        String charClue = dictionary.getCharsClue(answer, userWord);

        Map<Character, Integer> confirmedCounts = new HashMap<>();
        Set<Character> hasUnconfirmed = new HashSet<>();

        for (int i = 0; i < dictionary.WORDLE_WORD_LENGTH; i++) {
            char currentChar = userWord.charAt(i);
            char currentClue = charClue.charAt(i);

            if (currentClue == '+' || currentClue == '^') {
                if (confirmedCounts.containsKey(currentChar)) {
                    int count = confirmedCounts.get(currentChar);
                    confirmedCounts.put(currentChar, count + 1);
                } else {
                    confirmedCounts.put(currentChar, 1);
                }
            } else {
                hasUnconfirmed.add(currentChar);
            }
        }

        for (Character ch : confirmedCounts.keySet()) {
            int count = confirmedCounts.get(ch);

            if (hasUnconfirmed.contains(ch)) {
                clueKeeper.getRequiredCharCounts().put(ch, count);
            } else {
                if (clueKeeper.getRequiredCharCounts().containsKey(ch)) {
                    int oldCount = clueKeeper.getRequiredCharCounts().get(ch);

                    if (count > oldCount) {
                        clueKeeper.getRequiredCharCounts().put(ch, count);
                    }
                } else {
                    clueKeeper.getRequiredCharCounts().put(ch, count);
                }
            }
        }

        for (Character ch : hasUnconfirmed) {
            if (!confirmedCounts.containsKey(ch)) {
                clueKeeper.getRequiredCharCounts().put(ch, 0);
            }
        }

        for (int i = 0; i < dictionary.WORDLE_WORD_LENGTH; i++) {
            char currentChar = userWord.charAt(i);
            char currentClue = charClue.charAt(i);

            if (currentClue == '+') {
                clueKeeper.getCorrectCharPosition().put(currentChar, i);
            }

            if (currentClue == '^') {
                if (clueKeeper.getIncorrectCharPositions().containsKey(currentChar)) {
                    clueKeeper.getIncorrectCharPositions().get(currentChar).add(i);
                } else {
                    Set<Integer> positions = new HashSet<>();
                    positions.add(i);
                    clueKeeper.getIncorrectCharPositions().put(currentChar, positions);
                }
            }
        }
    }

    private List<String> updateAvailableWords(){
        List<String> words = new ArrayList<>(availableWords);
        for (char ch : clueKeeper.getRequiredCharCounts().keySet()) {
            int valueOfChar = clueKeeper.getRequiredCharCounts().get(ch);

            if (clueKeeper.getRequiredCharCounts().get(ch) == 0) {
                words.removeIf(word -> word.indexOf(ch) != -1);
            }

            if (clueKeeper.getRequiredCharCounts().get(ch) > 0) {
                words.removeIf(word -> getTotalChar(word, ch) != valueOfChar);
            }
        }

        Map<Character, Set<Integer>> incorrectChars = clueKeeper.getIncorrectCharPositions();
            if (!incorrectChars.isEmpty()){
                for (char ch : incorrectChars.keySet()){
                    Set<Integer> currentSet = incorrectChars.get(ch);
                    for (int index : currentSet){
                        words.removeIf(word -> word.indexOf(ch) == index);
                    }
                }
            }

        Map<Character, Integer> correctCharPosition = clueKeeper.getCorrectCharPosition();
            if (!correctCharPosition.isEmpty()){
                for (char ch : correctCharPosition.keySet()){
                    int index = correctCharPosition.get(ch);
                    words.removeIf(word -> word.charAt(index) != ch);
                }
            }

        return words;
    }

    private int getTotalChar(String word, char target) {
        int count = 0;
        for (char ch : word.toCharArray()){
            if (ch == target)
                count++;
        }
        return count;
    }

    private String getWordFromAvailable() {
        return availableWords.get(rand.nextInt(availableWords.size()));
    }

    public void startGame() throws EnglishWordException {
        Scanner scanner = new Scanner(System.in);
        while (steps != 0){
            String userWord = scanner.nextLine();


            if (userWord.isBlank()) {
                userWord = getWordFromAvailable();
                System.out.println(userWord);
            }

            fillClueKeeper(userWord);
            String clue = dictionary.getCharsClue(answer, userWord);
            System.out.println(clue);
            availableWords = updateAvailableWords();
            steps--;
        }
    }

    public int getSteps() {
        return steps;
    }
}
