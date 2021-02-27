package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {
    private IDeque<K> keys;
    private IDeque<V> values;
    private int size;

    public MoveToFrontDictionary() {
        this.keys = new LinkedDeque<>();
        this.values = new LinkedDeque<>();
        this.size = 0;
    }

    @Override
    public V remove(K key) {
        IDeque<K> tempKey = new LinkedDeque<>();
        IDeque<V> tempVal = new LinkedDeque<>();
        while(keys.peekFront() != key){
            if(keys.size() == 0){
                keys = tempKey;
                values = tempVal;
                return null;
            }
            tempKey.addFront(keys.removeFront());
            tempVal.addFront(values.removeFront());
        }
        keys.removeFront();
        V value = values.removeFront();
        int c = tempKey.size();
        for(int i = 0; i < c; i++){
            keys.addFront(tempKey.removeFront());
            values.addFront(tempVal.removeFront());
        }
        size--;
        return value;
    }

    @Override
    public V put(K key, V value) {
        if(keys.contains(key)){
            V ret = remove(key);
            keys.addFront(key);
            values.addFront(value);
            size++;
            return ret;
        }
        keys.addFront(key);
        values.addFront(value);
        size++;
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    @Override
    public boolean containsValue(V value) {
        return values.contains(value);
    }

    @Override
    public int size() {
        return keys.size();
    }

    @Override
    public ICollection<K> keySet() {
        return keys;
    }

    @Override
    public ICollection<V> values() {
        return values;
    }

    public V get(K key) {
        for(int i = 0; i < keys.size(); i++){
            if(keys.peek().equals(key)){
                return values.peek();
            }
            keys.add(keys.removeBack());
            values.add(values.removeBack());
        }
        return null;
    }


    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
