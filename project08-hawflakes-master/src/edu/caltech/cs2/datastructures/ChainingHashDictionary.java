package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.*;

import java.util.Iterator;
import java.util.function.Supplier;

public class ChainingHashDictionary<K, V> implements IDictionary<K, V> {
    private Supplier<IDictionary<K, V>> chain;
    private Object [] hashTable;
    private int [] sizes = {43,263,1217, 7331,31973,64661,101149,126949,157307,206779,269939,332561,399989,543769,603257,691841,996019};
    private int size;
    private int sizeLen;
    private double load;

    public ChainingHashDictionary(Supplier<IDictionary<K, V>> chain) {
        hashTable = new Object [sizes[0]];
        this.size = 0;
        this.sizeLen = 0;
        this.chain = chain;
        load = 0.5;
    }

    private void resize(){
        sizeLen++;
        Object [] tempHash = new Object[sizes[sizeLen]];
        for(Object o: hashTable){
            if(o != null){
                for(K key: ((IDictionary<K, V>)o).keySet()){
                    int hash = Math.abs(key.hashCode() % tempHash.length);
                    if(tempHash[hash] == null){
                        tempHash[hash] = chain.get();
                    }
                    IDictionary<K, V> ret = (IDictionary<K, V>) tempHash[hash];
                    ret.put(key, ((IDictionary<K, V>)o).get(key));
                }
            }
        }
        hashTable = tempHash;
    }
    /**
     * @param key
     * @return value corresponding to key
     */
    @Override
    public V get(K key) {
        int hash = Math.abs(Math.abs(Math.abs(key.hashCode() % hashTable.length)));
        if(hashTable[hash] == null) {
            return null;
        }
        IDictionary<K, V> temp = (IDictionary<K, V>)hashTable[hash];
        return temp.get(key);
    }

    @Override
    public V remove(K key) {
        int hash = Math.abs(key.hashCode() % hashTable.length);
        IDictionary<K, V> temp = (IDictionary<K, V>)hashTable[hash];
        if(temp == null) {
            return null;
        }
        V val = temp.remove(key);
        if(val != null){
            size--;
        }
        return val;
    }

    @Override
    public V put(K key, V value) {
        if ((double)(size + 1)/hashTable.length > load){
            resize();
        }
        int hash = Math.abs(key.hashCode() % hashTable.length);
        if(hashTable[hash] == null){
            hashTable[hash] = chain.get();
        }
        V val = ((IDictionary<K, V>)hashTable[hash]).get(key);
        if (val == null){
            size++;
        }
        ((IDictionary<K, V>)hashTable[hash]).put(key, value);
        return val;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * @param value
     * @return true if the HashDictionary contains a key-value pair with
     * this value, and false otherwise
     */
    @Override
    public boolean containsValue(V value) {
        return values().contains(value);
    }

    /**
     * @return number of key-value pairs in the HashDictionary
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        LinkedDeque<K> keys = new LinkedDeque<>();
        for(int i = 0; i < hashTable.length; i++){
            if(hashTable[i] != null){
                IDictionary<K, V> temp = (IDictionary<K, V>)hashTable[i];
                for(K key: temp.keys()){
                    keys.add(key);
                }
            }
        }
        return keys;
    }

    @Override
    public ISet<K> keySet() {
        ChainingHashSet<K> keys = new ChainingHashSet<>();
        for(int i = 0; i < hashTable.length; i++){
            if(hashTable[i] != null){
                IDictionary<K, V> temp = (IDictionary<K, V>)hashTable[i];
                for(K key: temp.keySet()){
                    keys.add(key);
                }
            }
        }
        return keys;
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> values = new LinkedDeque<>();
        for(int i = 0; i < hashTable.length; i++){
            if(hashTable[i] != null){
                IDictionary<K, V> temp = (IDictionary<K, V>)hashTable[i];
                for(V val: temp.values()){
                    values.add(val);
                }
            }
        }
        return values;
    }

    /**
     * @return An iterator for all entries in the HashDictionary
     */
    @Override
    public Iterator<K> iterator() {
        return this.keys().iterator();
    }

}
