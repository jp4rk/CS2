package edu.caltech.cs2.project02.choosers;

import com.sun.source.tree.Tree;
import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EvilHangmanChooser implements IHangmanChooser {

  public static String filez = "data/scrabble.txt";
  private static final Random RANDOM = new Random();
  private int maxGuesses;
  private SortedSet<Character> letterset;
  private SortedSet<String> sortedSet;

  public EvilHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {

    this.maxGuesses = maxGuesses;
    this.letterset = new TreeSet<>();

    Scanner scrab = new Scanner(new File(filez));
    SortedSet<String> sortedset = new TreeSet<>();

    while (scrab.hasNextLine()) {
      String line = scrab.nextLine();
      if (line.length() == wordLength) {
        sortedset.add(line);
      }
    }

    if (wordLength < 1 | maxGuesses < 1) {
      throw new IllegalArgumentException("Invalid number of wordlength or maxguesses");
    }


    if (sortedset.size() == 0) {
      throw new IllegalStateException("No words found of wordlength");
    }

    this.sortedSet = sortedset;
  }

  public int makeGuess(char letter) {

    if (this.maxGuesses < 1) {
      throw new IllegalStateException("Number of guesses left is not at least one!");
    }

    if (Character.isLowerCase(letter)) {
      if (this.letterset.contains(letter)) {
        throw new IllegalArgumentException("This character was guessed previously.");
      }
      else {
        this.letterset.add(letter);
      }
    }

    else {
      throw new IllegalArgumentException("All guesses should be lowercase letters.");
    }

    Map<String, SortedSet> mapz = new TreeMap<>();

    for (String k: this.sortedSet) {
      String guessAns = "";
      for (int i = 0; i < k.length(); i++) {
        if (this.letterset.contains(k.charAt(i))) {
          guessAns += k.charAt(i);
        } else {
          guessAns += "-";
        }
      }
      if (!mapz.containsKey(guessAns)) {
        SortedSet<String> newList = new TreeSet<>();
        mapz.put(guessAns, newList);
      }
      mapz.get(guessAns).add(k);
    }

    SortedSet<String> bestList = new TreeSet<>();

    for (SortedSet sortset: mapz.values()) {
      if (sortset.size() > bestList.size()) {
        bestList = sortset;
      }
    }

    this.sortedSet = bestList;


    int var = 0;

    for (int i = 0; i < this.sortedSet.first().length(); i++) {
      if (this.sortedSet.first().charAt(i) == letter) {
        var += 1;
      }
    }

    if (var == 0) {
      this.maxGuesses -= 1;
    }

    return var;

  }

  @Override
  public boolean isGameOver() {
    int count = 0;

    for (int i = 0; i < this.sortedSet.first().length(); i++) {
      if (this.letterset.contains(this.sortedSet.first().charAt(i))) {
        count += 1;
      }
    }

    if (count == this.sortedSet.first().length()) {
      return true;
    }

    if (this.maxGuesses == 0) {
      return true;
    }

    else {
      return false;
    }

  }

  @Override
  public String getPattern() {
    String guessAns = "";
    for (int i = 0; i < this.sortedSet.first().length(); i++) {
      if (this.letterset.contains(this.sortedSet.first().charAt(i))){
        guessAns += this.sortedSet.first().charAt(i);
      }
      else {
        guessAns += "-";
      }
    }
    return guessAns;
  }

  @Override
  public SortedSet<Character> getGuesses() {
    return this.letterset;
  }

  @Override
  public int getGuessesRemaining() {
    return this.maxGuesses;
  }

  @Override
  public String getWord() {
    this.maxGuesses = 0;
    return this.sortedSet.first();
  }
}