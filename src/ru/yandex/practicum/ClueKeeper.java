package ru.yandex.practicum;

import java.util.*;

public class ClueKeeper {
    private Map<Character, Integer> requiredCharCounts = new HashMap<>();
    private Map<Character, Set<Integer>> correctCharPosition = new HashMap<>();
    private Map<Character, Set<Integer>> incorrectCharPositions = new HashMap<>();
    private Set<Character> bannedChars = new HashSet<>();

    public Map<Character, Set<Integer>> getCorrectCharPosition() {
        return correctCharPosition;
    }

    public Map<Character, Set<Integer>> getIncorrectCharPositions() {
        return incorrectCharPositions;
    }

    public Map<Character, Integer> getRequiredCharCounts() {
        return requiredCharCounts;
    }

    public Set<Character> getBannedChars() {
        return bannedChars;
    }
}
