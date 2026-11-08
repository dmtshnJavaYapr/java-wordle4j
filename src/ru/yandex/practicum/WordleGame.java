package ru.yandex.practicum;

import java.io.PrintWriter;
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
    private PrintWriter log;

    private List<String> userWords = new ArrayList<>();
    private List<String> availableWords;
    Random rand = new Random();

    public WordleGame(WordleDictionary wordleDictionary, PrintWriter log) {
        this.dictionary = wordleDictionary;
        answer = dictionary.getRandomWord();
        availableWords = new ArrayList<>(dictionary.getWords());
        clueKeeper = new ClueKeeper();
        this.log = log;
    }

    public void fillClueKeeper(String userWord){
        String clue = dictionary.getCharsClue(answer, userWord);
        Map<Character, Integer> requiredCharCounts = clueKeeper.getRequiredCharCounts();
        Set<Character> bannedChars = clueKeeper.getBannedChars();
        Set<Character> charWithCaret = new HashSet<>();
        Set<Character> doNotBan = new HashSet<>();

        for (int i = 0; i < dictionary.WORDLE_WORD_LENGTH; i++) {
            char currentChar = userWord.charAt(i);
            char clueChar = clue.charAt(i);
            if (clueChar == '+') {
                Map<Character, Set<Integer>> correctCharPosition = clueKeeper.getCorrectCharPosition();
                if (correctCharPosition.containsKey(currentChar))
                    correctCharPosition.get(currentChar).add(i);

                else {
                    Set<Integer> indexes = new HashSet<>(List.of(i));
                    correctCharPosition.put(currentChar, indexes);
                }
                doNotBan.add(currentChar);
            }

            if (clueChar == '^') {
                if (!charWithCaret.contains(currentChar)) {
                    int count = 0;
                    for (int j = 0; j < dictionary.WORDLE_WORD_LENGTH; j++) {
                        if (currentChar == userWord.charAt(j) && (clue.charAt(j) == '^' || clue.charAt(j) == '+'))
                            count++;
                    }

                    if (requiredCharCounts.containsKey(currentChar) &&
                            requiredCharCounts.get(currentChar) < count) {
                        requiredCharCounts.replace(currentChar, count);
                    }

                    if (!requiredCharCounts.containsKey(currentChar))
                        requiredCharCounts.put(currentChar, count);
                }

                charWithCaret.add(currentChar);
                doNotBan.add(currentChar);
            }
        }
        for (int i = 0; i < dictionary.WORDLE_WORD_LENGTH; i++){
            char currentChar = userWord.charAt(i);
            char clueChar = clue.charAt(i);

            if (clueChar == '-' && !doNotBan.contains(currentChar)){
                bannedChars.add(currentChar);
            }
        }

    }

    public void updateAvailableWords(){
        List<String> words = new ArrayList<>(availableWords);
        Map<Character, Integer> requiredCharCounts = clueKeeper.getRequiredCharCounts();
        Map<Character, Set<Integer>> correctCharPosition = clueKeeper.getCorrectCharPosition();
        Set<Character> bannedChars = clueKeeper.getBannedChars();

        // '-'
        for (char ch : bannedChars) {
            words.removeIf(word -> word.contains(Character.toString(ch)));
        }

        // +'
        for (char ch : correctCharPosition.keySet()){
            for (int index : correctCharPosition.get(ch)) {
                words.removeIf(word -> word.charAt(index) != ch);
            }
        }

        // '^'
        for (char ch : requiredCharCounts.keySet()){
                words.removeIf(word -> getTotalChar(word, ch) < requiredCharCounts.get(ch));
        }

        availableWords = words;
    }

    private int getTotalChar(String word, char target) {
        int count = 0;
        for (char ch : word.toCharArray()){
            if (ch == target)
                count++;
        }
        return count;
    }

    private int getTotalCharWithCaret(String clue, String word){
        return 0;
    }

    public String getWordFromAvailable() {
        return availableWords.get(rand.nextInt(availableWords.size()));
    }

    public int getSteps() {
        return steps;
    }

    public String getAnswer() {
        return answer;
    }

    public void nextStep(){
        steps--;
    }

    public int getSizeAvailableWords(){
        return availableWords.size();
    }

    public List<String> getAvailableWords(){
        return availableWords;
    }
}
