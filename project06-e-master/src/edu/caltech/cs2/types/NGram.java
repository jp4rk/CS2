package edu.caltech.cs2.types;

import edu.caltech.cs2.datastructures.IterableString;
import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.interfaces.IDeque;

import java.util.Iterator;

public class NGram implements Iterable<String>, Comparable<NGram> {
    public static final String NO_SPACE_BEFORE = ",?!.-,:'";
    public static final String NO_SPACE_AFTER = "-'><=";
    public static final String REGEX_TO_FILTER = "”|\"|“|\\(|\\)|\\*";
    public static final String DELIMITER = "\\s+|\\s*\\b\\s*";
    private IDeque<String> data;

    public static String normalize(String s) {
        return s.replaceAll(REGEX_TO_FILTER, "").strip();
    }

    public NGram(IDeque<String> x) {
        this.data = new LinkedDeque<>();
        for (int i = 0; i < x.size(); i++) {
            this.data.addBack(x.peekFront());
            x.addBack(x.removeFront());
        }
    }

    public NGram(String data) {
        this(normalize(data).split(DELIMITER));
    }

    public NGram(String[] data) {
        this.data = new LinkedDeque<>();
        for (String s : data) {
            s = normalize(s);
            if (!s.isEmpty()) {
                this.data.addBack(s);
            }
        }
    }

    public NGram next(String word) {
        String[] data = new String[this.data.size()];
        for (int i = 0; i < data.length - 1; i++) {
            this.data.addBack(this.data.removeFront());
            data[i] = this.data.peekFront();
        }
        this.data.addBack(this.data.removeFront());
        data[data.length - 1] = word;
        return new NGram(data);
    }

    public String toString() {
        String result = "";
        String prev = "";
        for (String s : this.data) {
            result += ((NO_SPACE_AFTER.contains(prev) || NO_SPACE_BEFORE.contains(s) || result.isEmpty()) ?  "" : " ") + s;
            prev = s;
        }
        return result.strip();
    }

    @Override
    public Iterator<String> iterator() {
        return this.data.iterator();
    }


    @Override
    public int compareTo(NGram other) {
        IDeque<String> s = ((NGram) other).data;
        Iterator<String> ngram = s.iterator();
        Iterator<String> d = data.iterator();
        while(ngram.hasNext() && d.hasNext()) {
            int comp = d.next().compareTo(ngram.next());
            if(comp != 0){
                return comp;
            }
        }
        if(ngram.hasNext()){
            return -1;
        }
        else if(d.hasNext()){
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NGram) || ((NGram) o).data.size() != this.data.size()) {
            return false;
        }
        IDeque<String> s = ((NGram) o).data;
        Iterator<String> ngram = s.iterator();
        Iterator<String> d = data.iterator();
        while(ngram.hasNext()) {
            if(!d.next().equals(ngram.next())){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 0;
        int add = 1;
        for(String s: this.data){
            result += add*s.hashCode();
            add *= prime;
        }

        if (result < 0){
            result *= -1;
        }

        return result;
    }
}