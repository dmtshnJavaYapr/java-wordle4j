package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    public final int WORDLE_WORD_LENGTH = 5;

    private List<String> words;
    private PrintWriter log;

    public WordleDictionary(List<String> words, PrintWriter log){
        this.words = words;
        this.log = log;
    }

    private Random random = new Random();
    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

    public List<String> getWords() {
        return words;
    }

    public void checkUserWordInDict(String userWord) {
        if (!words.contains(StringNormalizer.normalizeString(userWord)))
            throw new WordIsNotFoundInDict("Такого слова в словаре не существует");

    }

    public void checkWordRules(String userWord) throws WordsLengthIsNotFive {
        if (userWord.length() != 5)
            throw new WordsLengthIsNotFive("Некорректная длина слова");
    }

    public void checkIsWordNotRussian(String userWord) throws EnglishWordException {
        if (userWord.matches("^[a-zA-Z]+$"))
            throw new EnglishWordException("Слово должно быть на русском языке");
    }

    public String getCharsClue(String answer, String userWord) {
        StringBuilder userWordSB = new StringBuilder(userWord);
        StringBuilder clue = new StringBuilder(new String("-----"));
        Map<Character, Integer> charCounter = fillCharMap(answer);

        clue = setPlusForCorrectCharInOwnPlace(answer, userWordSB, clue, charCounter);
        clue = setCaretForCharThatHasInAnswerButNotOwnPlace(answer, userWordSB, clue, charCounter);


        return clue.toString();
    }

    private StringBuilder setPlusForCorrectCharInOwnPlace(String answer, StringBuilder userWordSB, StringBuilder clue,
                                               Map<Character, Integer> charCounter){
        for (int i = 0; i < WORDLE_WORD_LENGTH; i++) {
            char target = userWordSB.charAt(i);
            if (answer.charAt(i) == target) {
                clue.setCharAt(i, '+');
                charCounter.replace(target, charCounter.get(target) - 1);
            }
        }

        return clue;
    }

    private StringBuilder setCaretForCharThatHasInAnswerButNotOwnPlace(
            String answer, StringBuilder userWordSB, StringBuilder clue,
            Map<Character, Integer> charCounter){

        for (int i = 0; i < WORDLE_WORD_LENGTH; i++) {
            char target = userWordSB.charAt(i);
            if (clue.charAt(i) == '+')
                continue;

            if (answer.contains(String.valueOf(target))){
                if (charCounter.get(target) > 0){
                    clue.setCharAt(i, '^');
                    charCounter.replace(target, charCounter.get(target) - 1);
                }
            }

        }
        return clue;
    }

    private Map<Character, Integer> fillCharMap(String answer){
        Map<Character, Integer> charCounter = new HashMap<>();
        for (Character ch : answer.toCharArray()) {
            if (charCounter.containsKey(ch)){
                int charValue = charCounter.get(ch);
                charCounter.replace(ch, charValue + 1);
            }

            else
                charCounter.put(ch, 1);

        }
        return charCounter;
    }
}
