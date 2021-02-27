package edu.caltech.cs2.project02.guessers;

import edu.caltech.cs2.project02.interfaces.IHangmanGuesser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AIHangmanGuesser implements IHangmanGuesser {

  private static String filez = "data/scrabble.txt";

  @Override
  public char getGuess(String pattern, Set<Character> guesses) throws FileNotFoundException {

    char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    char[] alphabet2 = new char[26 - guesses.size()];

    int counts = 0;

    for (char i : alphabet) {
      if (!guesses.contains(i)) {
        alphabet2[counts] = i;
        counts++;
      }
    }


    Scanner scrab = new Scanner(new File(filez));
    SortedSet<String> sortedset = new TreeSet<>();

    while (scrab.hasNextLine()) {
      String line = scrab.nextLine();
      if (line.length() == pattern.length()) {
        sortedset.add(line);
      }
    }

    SortedSet<String> sortedset2 = new TreeSet<>();


    for (String k : sortedset) {
      boolean check = true;
      for (int h = 0; h < pattern.length(); h++) {
        if (pattern.charAt(h) == '-' && guesses.contains(k.charAt(h))) {
          check = false;
          break;
        }
        else if (pattern.charAt(h) != '-') {
          if (pattern.charAt(h) != k.charAt(h)) {
            check = false;
            break;
          }
        }
      }
      if (check) {
        sortedset2.add(k);
      }
    }

    Map<Character, Integer> mapz = new HashMap<>();

    for (String k : sortedset2) {
      for (int a = 0; a < k.length(); a++) {
        for (char h : alphabet2) {
          if (k.charAt(a) == h) {
            if (mapz.get(h) == null) {
              mapz.put(h, 1);
            } else {
              mapz.put(h, mapz.get(h) + 1);
            }
          }
        }
      }
    }

    char best = ' ';
    int bestint = 0;

    for (char b : mapz.keySet()) {
      if (bestint < mapz.get(b)) {
        best = b;
        bestint = mapz.get(b);
      }

    }
    return best;
  }
}
