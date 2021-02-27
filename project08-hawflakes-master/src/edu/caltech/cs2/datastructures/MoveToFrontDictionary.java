package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;

public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {
    private ListNode<K,V> head;
    private ListNode<K, V> tail;
    private int size;

    private static class ListNode<K,V> {
        public K key;
        public V value;
        public ListNode<K,V> prev;
        public ListNode<K, V> next;

        public ListNode(K key, V value) {
            this(key, value, null, null);
        }

        public ListNode(K key, V value, ListNode<K, V> next, ListNode<K,V> prev) {
            this.key = key;
            this.next = next;
            this.value = value;
            this.prev = prev;
        }
    }


    public MoveToFrontDictionary() {
        this.head = null;
        this.size=0;
        this.tail = null;
    }

    @Override
    public V remove(K key) {
        if (this.size == 0) {
            return null;
        }
        if(this.head.key.equals(key)){
            V val = this.head.value;
            this.head = this.head.next;
            this.size--;
            return val;
        }
        V val = helper(key, this.head);
        if(val != null){
            this.size--;
        }
        return val;


    }
    private V helper(K key, ListNode<K, V> curr){
        if(curr.next == null){
            return null;
        }
        if(curr.next.key.equals(key)){
            V val = curr.next.value;
            curr.next = curr.next.next;
            return val;
        }
        return helper(key, curr.next);
    }
    @Override
    public V put(K key, V value) {
        if (this.containsKey(key)) {
            V val = this.get(key);
            this.head.value = value;
            return val;
        } else {
            ListNode<K, V> curr = new ListNode<K, V>(key, value);
            if (this.size == 0) {
                curr.next = null;
            } else {
                curr.next = this.head;
            }
            this.size++;
            this.head = curr;
            return null;
        }
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
    public ICollection<K> keys() {
        ICollection<K> ks = new LinkedDeque<K>();
        ks = keyHelp(this.head, ks);
        return ks;
    }

    private ICollection<K> keyHelp(ListNode<K,V> curr, ICollection<K> ks){
        if(curr == null){
            return ks;
        }
        ks.add(curr.key);
        return keyHelp(curr.next, ks);
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> vs = new LinkedDeque<V>();
        vs = valHelp(this.head, vs);
        return vs;
    }
    private ICollection<V> valHelp(ListNode<K,V> curr, ICollection<V> vs){
        if(curr == null){
            return vs;
        }
        vs.add(curr.value);
        return valHelp(curr.next, vs);
    }


    public V get(K key) {
        if(this.size ==0){
            return null;
        }
        ListNode<K, V> prev = this.head;
        ListNode<K, V> curr = this.head.next;
        if(prev.key.equals(key)){
            return prev.value;
        }
        for(int i =0; i< this.size; i++){
            if(curr != null && curr.key.equals(key)){
                prev.next = curr.next;
                curr.next = this.head;
                this.head=curr;
                return curr.value;
            }
            prev = curr;
            if(curr!=null){
                curr=curr.next;
            }
        }
        return null;
    }

    public class Linked implements Iterator<K> {
        private ListNode<K, V> x = MoveToFrontDictionary.this.head;
        @Override
        public boolean hasNext() {
            return this.x != null;
        }

        @Override
        public K next() {
            if (hasNext()){
                K key = x.key;
                x = this.x.next;
                return key;
            }
            return null;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new Linked();
    }
}



