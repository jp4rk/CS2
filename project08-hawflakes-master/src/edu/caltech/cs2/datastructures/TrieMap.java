package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ISet;
import edu.caltech.cs2.interfaces.ITrieMap;

import java.util.*;
import java.util.function.Function;
import java.util.HashMap;
import java.util.Map;

public class TrieMap<A, K extends Iterable<A>, V> implements ITrieMap<A, K, V> {
    private TrieNode<A, V> root;
    private Function<IDeque<A>, K> collector;
    private int size;
    private ICollection<K> keys;

    public TrieMap(Function<IDeque<A>, K> collector) {
        this.root = new TrieNode<>();
        this.collector = collector;
        this.size = 0;
        this.keys = new LinkedDeque<>();
    }


    @Override
    public boolean isPrefix(K key) {
        TrieNode<A, V> curr = this.root;
        for(A c: key){
            if(!curr.pointers.containsKey(c)){
                return false;
            }
            curr = curr.pointers.get(c);
        }
        return true;
    }

    @Override
    public IDeque<V> getCompletions(K prefix) {
        TrieNode<A, V> curr = this.root;
        IDeque<V> comp = new LinkedDeque<>();
        if(!isPrefix(prefix)){
            return comp;
        }
        for(A c: prefix){
            curr = curr.pointers.get(c);
        }
        getValues(comp, curr);
        return comp;
    }

    @Override
    public void clear() {
        this.root = new TrieNode<>();
        size = 0;
    }

    @Override
    public V get(K key) {
        TrieNode<A, V> curr = this.root;
        for(A c: key){
            if(!curr.pointers.containsKey(c)){
                return null;
            }
            curr = curr.pointers.get(c);
        }
        return curr.value;
    }

    @Override
    public V remove(K key) {
        TrieNode<A, V> curr = this.root;
        V ret = get(key);
        Iterator<A> chars = key.iterator();
        removeBranches(curr, chars);
        if(ret != null){
            size--;
        }
        return ret;
    }

    private boolean removeBranches(TrieNode<A, V> curr, Iterator<A> chars){
        if (curr == null){
            return false;
        }
        if(!chars.hasNext()){
            curr.value = null;
            if(curr.pointers.size() == 0){
                curr = null;
                return true;
            }
        }
        else{
            A c = chars.next();
            boolean exists = removeBranches(curr.pointers.get(c), chars);
            if(exists){
                curr.pointers.remove(c);
            }
            if(curr.pointers.size() == 0 && curr.value == null) {
                curr = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public V put(K key, V value) {
        TrieNode<A, V> curr = this.root;
        for(A c: key){
            if(!curr.pointers.containsKey(c)){
                curr.pointers.put(c, new TrieNode<>());
            }
            curr = curr.pointers.get(c);
        }
        V ret = curr.value;
        if (curr.value == null){
            size++;
            keys.add(key);
        }

        if (curr.value == null || !curr.value.equals(value)){
            curr.value = value;
        }
        return ret;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        return values().contains(value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ICollection<K> keys() {
        return (ICollection<K>)keySet();
    }

    @Override
    public ISet<K> keySet() {
        return (ISet<K>) keys;
    }

    @Override
    public ICollection<V> values() {
        TrieNode<A,V> curr = this.root;
        ICollection<V> value = new LinkedDeque<>();
        return getValues(value, curr);
    }

    private ICollection<V> getValues(ICollection<V> value, TrieNode<A, V> curr){
        if(curr.value != null){
            value.add(curr.value);
        }
        if(!curr.pointers.isEmpty()){
            for(A c: curr.pointers.keySet()){
                getValues(value, curr.pointers.get(c));
            }
        }
        return value;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    private static class TrieNode<A, V> {
        public final Map<A, TrieNode<A, V>> pointers;
        public V value;

        public TrieNode() {
            this(null);
        }

        public TrieNode(V value) {
            this.pointers = new HashMap<A, TrieNode<A, V>>();
            this.value = value;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            if (this.value != null) {
                b.append("[" + this.value + "]-> {\n");
                this.toString(b, 1);
                b.append("}");
            }
            else {
                this.toString(b, 0);
            }
            return b.toString();
        }

        private String spaces(int i) {
            StringBuilder sp = new StringBuilder();
            for (int x = 0; x < i; x++) {
                sp.append(" ");
            }
            return sp.toString();
        }

        protected boolean toString(StringBuilder s, int indent) {
            boolean isSmall = this.pointers.entrySet().size() == 0;

            for (Map.Entry<A, TrieNode<A, V>> entry : this.pointers.entrySet()) {
                A idx = entry.getKey();
                TrieNode<A, V> node = entry.getValue();

                if (node == null) {
                    continue;
                }

                V value = node.value;
                s.append(spaces(indent) + idx + (value != null ? "[" + value + "]" : ""));
                s.append("-> {\n");
                boolean bc = node.toString(s, indent + 2);
                if (!bc) {
                    s.append(spaces(indent) + "},\n");
                }
                else if (s.charAt(s.length() - 5) == '-') {
                    s.delete(s.length() - 5, s.length());
                    s.append(",\n");
                }
            }
            if (!isSmall) {
                s.deleteCharAt(s.length() - 2);
            }
            return isSmall;
        }
    }
}
