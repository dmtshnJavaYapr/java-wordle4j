package ru.yandex.practicum;

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

    private final int WORDLE_WORD_LENGTH = 5;

    private List<String> words;

    public WordleDictionary(List<String> words){
        this.words = words;
    }

    private Random random = new Random();
    public String getAnswer(){
        return words.get(random.nextInt(words.size()));
    }

    public List<String> getWords() {
        return words;
    }

    public boolean checkUserWordInDict(String userWord){
        return words.contains(StringNormalizer.normalizeString(userWord));
    }

    public boolean checkWordRules(String userWord){
        return userWord.length() == 5;
    }

    public String getCharsClue(String answer, String userWord){
        StringBuilder answerSB = new StringBuilder(answer);
        StringBuilder userWordSB = new StringBuilder(userWord);
        StringBuilder clue = new StringBuilder(new String("-----"));
        Map<Character, Integer> charCounter = fillCharMap(answer);

        for (int i = 0; i < WORDLE_WORD_LENGTH; i++) {
            char target = userWordSB.charAt(i);
            if (answer.charAt(i) == target) {
                clue.setCharAt(i, '+');
                charCounter.replace(target, charCounter.get(target) - 1);
            }
        } // то что выше в отдельный метод упаковать

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

        } // также в отдельный метод можно швырять

        return null;
    }

    public Map<Character, Integer> fillCharMap(String answer){
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
