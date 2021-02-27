package edu.caltech.cs2.lab06;

import java.util.ArrayList;
import java.util.List;

public class AnagramGenerator {
    public static void printPhrases(String phrase, List<String> dictionary){
        if(phrase.length() <= 1){
            System.out.println("[]");
            return;
        }
        findWord(new LetterBag(phrase), dictionary, new ArrayList<>());
    }

    private static void findWord(LetterBag l, List<String> dictionary, List<String> acc){
        for(String s: dictionary){
            LetterBag temp = l.subtract(new LetterBag(s));
            if(temp == null){
                continue;
            }
            else if(temp.isEmpty()){
                acc.add(s);
                System.out.println(acc);
                acc.remove(s);
            }
            acc.add(s);
            findWord(temp, dictionary, acc);
            acc.remove(s);
        }
    }

    public static void printWords(String word, List<String> dictionary) {
        for(String s: dictionary){
            LetterBag temp = new LetterBag(word).subtract(new LetterBag(s));
            if(temp == null){
                continue;
            }
            else if(temp.isEmpty()){
                System.out.println(s);
            }
        }
    }
}
