
package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    @Test
    void testStringNormalizer() {
        String result = StringNormalizer.normalizeString("ЁЖИК");

        assertEquals("ежик", result);
    }

    @Test
    void testAllCharsCorrect() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("финик"),
                new PrintWriter(new StringWriter())
        );

        String clue = dictionary.getCharsClue("финик", "финик");

        assertEquals("+++++", clue);
    }

    @Test
    void testDifferentCharPositions() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("финик"),
                new PrintWriter(new StringWriter())
        );

        String clue = dictionary.getCharsClue("финик", "кифан");

        assertEquals("^+^-^", clue);
    }

    @Test
    void testRepeatedChars() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("банан"),
                new PrintWriter(new StringWriter())
        );

        String clue = dictionary.getCharsClue("банан", "анана");

        assertEquals("^^^^-", clue);
    }

    @Test
    void testUpdateAvailableWords() {
        List<String> words = new ArrayList<>(List.of(
                "финик",
                "банан",
                "лимон",
                "арбуз"
        ));

        WordleDictionary dictionary = new WordleDictionary(
                words,
                new PrintWriter(new StringWriter())
        );

        WordleGame game = new WordleGame(
                dictionary,
                new PrintWriter(new StringWriter())
        );

        String answer = game.getAnswer();

        game.fillClueKeeper(answer);
        game.updateAvailableWords();

        assertEquals(
                List.of(answer),
                game.getAvailableWords()
        );
    }
}
