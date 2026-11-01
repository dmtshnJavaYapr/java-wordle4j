package ru.yandex.practicum;

import java.util.*;

public class ClueKeeper {
    private Map<Character, Integer> requiredCharCounts = new HashMap<>();
    private Map<Character, Integer> correctCharPosition = new HashMap<>();
    private Map<Character, Set<Integer>> incorrectCharPositions = new HashMap<>();

    public Map<Character, Integer> getCorrectCharPosition() {
        return correctCharPosition;
    }

    public Map<Character, Set<Integer>> getIncorrectCharPositions() {
        return incorrectCharPositions;
    }

    public Map<Character, Integer> getRequiredCharCounts() {
        return requiredCharCounts;
    }
}
