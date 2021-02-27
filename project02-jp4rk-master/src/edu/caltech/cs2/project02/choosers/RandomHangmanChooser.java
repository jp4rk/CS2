package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RandomHangmanChooser implements IHangmanChooser {

  public static String filez = "data/scrabble.txt";
  private static final Random RANDOM = new Random();
  private final String ans;
  private int maxGuesses;
  private SortedSet<Character> letterset;


  public RandomHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {

    this.maxGuesses = maxGuesses;
    this.letterset = new TreeSet<>();

    Scanner scrab = new Scanner(new File(filez));
    SortedSet<String> sortedset = new TreeSet<>();

    while (scrab.hasNextLine()) {
      sortedset.add(scrab.nextLine());
    }

    if (wordLength < 1 | maxGuesses < 1) {
      throw new IllegalArgumentException("Invalid number of wordlength or maxguesses");
    }

    SortedSet<String> sortedset2 = new TreeSet<>();

    for (String k : sortedset) {
      if (k.length() == wordLength) {
        sortedset2.add(k);
      }
    }

    if (sortedset2.size() == 0 ) {
      throw new IllegalStateException("No words found of wordlength");
    }


    int count = RANDOM.nextInt(sortedset2.size());
    int var = 0;
    String ans = "";

    for (String s : sortedset2) {
      if (var == count) {
        ans = s;
      }
      var++;
    }

    this.ans = ans;

  }

  public int makeGuess(char letter) {
    int var = 0;

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


    for (int i = 0; i < this.ans.length(); i++) {
      if (this.ans.charAt(i) == letter) {
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


    for (int i = 0; i < this.ans.length(); i++) {
      if (this.letterset.contains(this.ans.charAt(i))) {
        count += 1;
      }
    }

    if (count == this.ans.length()) {
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
    for (int i = 0; i < this.ans.length(); i++) {
      if (this.letterset.contains(this.ans.charAt(i))){
        guessAns += this.ans.charAt(i);
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
    return this.ans;

  }
}