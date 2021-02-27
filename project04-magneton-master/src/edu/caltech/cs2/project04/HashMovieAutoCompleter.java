package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.interfaces.IDeque;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

public class HashMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static Map<String, IDeque<String>> titles = new HashMap<>();

    public static void populateTitles() {
        for (String s : AbstractMovieAutoCompleter.getIDMap().keySet()){
            IDeque<String> temp = new ArrayDeque<>();
            String name = s;
            while(s.contains(" ")) {
                temp.addBack(s.toLowerCase());
                s = s.substring(s.indexOf(' ') + 1);
            }
            temp.addBack(s.toLowerCase());
            titles.put(name, temp);
        }
    }

    public static IDeque<String> complete(String term) {
        IDeque<String> ret = new ArrayDeque<>();
        for(String s: titles.keySet()){
            IDeque<String> curr = titles.get(s);
            for(String t: curr){
                if(t.contains(term)){
                    int i = t.indexOf(term);
                    boolean left = false;
                    boolean right = false;
                    if(i == 0){
                        left = true;
                    }
                    else if(t.substring(i - 1, i).equals(" ")){
                        left = true;
                    }
                    if(i == t.length() - term.length()){
                        right = true;
                    }
                    else if(t.substring(i + term.length(), i + term.length() + 1).equals(" ")){
                        right = true;
                    }
                    if (left && right) {
                        ret.add(s);
                        break;
                    }
                }
            }
        }
        return ret;
    }
}
