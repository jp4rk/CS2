package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.datastructures.TrieMap;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;

public class TrieMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static ITrieMap<String, IDeque<String>, IDeque<String>> titles = new TrieMap<>((IDeque<String> s) -> s);

    public static void populateTitles() {
        for (String s : AbstractMovieAutoCompleter.getIDMap().keySet()){
            IDeque<String> suffix = new LinkedDeque<>();
            String [] words = s.split(" ");
            for (String w : words){
                suffix.addBack(w.toLowerCase());
            }
            int len = suffix.size();
            for(int i = 0; i < len; i++) {
                IDeque<String> title = titles.get(suffix);
                if (title == null) {
                    IDeque<String> temp = new LinkedDeque<>();
                    temp.add(s);
                    titles.put(suffix, temp);
                }
                else {
                    title.add(s);
                }
                suffix.removeFront();
            }
        }
    }

    public static IDeque<String> complete(String term) {
        IDeque<String> possible = new LinkedDeque<>();
        IDeque<String> prefix = new LinkedDeque<>();
        String [] words = term.split(" ");
        for(String w: words){
            prefix.addBack(w.toLowerCase());
        }

        for(IDeque<String> curr: titles.getCompletions(prefix)){
            for(String w: curr){
                if(!possible.contains(w)){
                    possible.add(w);
                }
            }
        }
        return possible;
    }
}
