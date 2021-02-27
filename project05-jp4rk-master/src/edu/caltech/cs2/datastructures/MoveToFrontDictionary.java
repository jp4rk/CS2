package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.textgenerator.NGram;
import org.w3c.dom.Node;

import java.util.Dictionary;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {
    private int size;
    private IDeque<K> keys;
    private IDeque<V> values;

    public MoveToFrontDictionary() {
        this.size = 0;
        this.keys = new LinkedDeque<>();
        this.values = new LinkedDeque<>();
    }

    @Override
    public V remove(K key) {
        for (int i = 0; i < this.size; i ++) {
            if(this.keys.peek().equals(key)) {
                this.keys.removeBack();
                this.size--;
                return this.values.removeBack();
            }
            this.keys.add(this.keys.removeBack());
            this.values.add(this.values.removeBack());
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        for(int i = 0; i < this.size; i++) {
            if(this.keys.peek().equals(key)) {
                this.keys.removeBack();
                this.keys.add(key);
                this.values.add(value);
                return this.values.removeBack();
            }
            this.keys.add(this.keys.removeBack());
            this.values.add(this.values.removeBack());
        }
        this.keys.add(key);
        this.values.add(value);
        this.size++;
        return null;
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
        return this.size;
    }

    @Override
    public ICollection<K> keySet() {
        return this.keys;
    }

    @Override
    public ICollection<V> values() {
        return this.values;
    }


    @Override
    public V get(K key) {
        for(int i = 0; i < this.size; i++) {
            if(this.keys.peek().equals(key)){
                return this.values.peek();
            }
            this.keys.add(this.keys.removeBack());
            this.values.add(this.values.removeBack());
        }
        return null;
    }

    public class MoveIterator implements Iterator<K> {
        private int idx;

        public MoveIterator() {
            this.idx = 0;
        }

        @Override
        public boolean hasNext() {
            return this.idx < MoveToFrontDictionary.this.size;
        }

        @Override
        public K next() {
            this.idx++;
            MoveToFrontDictionary.this.keys.add(MoveToFrontDictionary.this.keys.peek());
            MoveToFrontDictionary.this.values.add(MoveToFrontDictionary.this.values.removeBack());
            return MoveToFrontDictionary.this.keys.removeBack();
        }

    }

    @Override
    public Iterator<K> iterator() {
        return new MoveIterator();
    }
}
