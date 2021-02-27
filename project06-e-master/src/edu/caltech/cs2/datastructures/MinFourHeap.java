package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IPriorityQueue;

import java.util.Iterator;

public class MinFourHeap<E> implements IPriorityQueue<E> {

    private static final int DEFAULT_CAPACITY = 5;

    private int size;
    private PQElement<E>[] data;
    private IDictionary<E, Integer> keyToIndexMap;

    /**
     * Creates a new empty heap with DEFAULT_CAPACITY.
     */
    public MinFourHeap() {
        this.size = 0;
        this.data = new PQElement[DEFAULT_CAPACITY];
        this.keyToIndexMap = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    private void percolateUp(int idx){
        int curr = idx;
        int par = (int)(Math.ceil((double)curr/4) - 1);
        if (par < 0){
            return;
        }
        PQElement<E> child = data[curr];
        PQElement<E> parent = data[par];
        while(child.priority < parent.priority){
            PQElement<E> temp = parent;
            data[(int)(Math.ceil((double)curr/4) - 1)] = child;
            keyToIndexMap.put(child.data, (int)(Math.ceil((double)curr/4) - 1));
            data[curr] = temp;
            keyToIndexMap.put(parent.data, curr);
            child = data[(int)(Math.ceil((double)curr/4) - 1)];
            curr = (int)(Math.ceil((double)curr/4) - 1);
            if((int)(Math.ceil((double)curr/4) - 1) < 0){
                return;
            }
            parent = data[(int)(Math.ceil((double)curr/4) - 1)];
        }
    }

    private void percolateDown(int idx){
        int curr = idx;
        int child = curr * 4;
        int small = data[curr].priority;

        if(child +  1 < size){
            if(data[child + 1].priority < small){
                small = data[child + 1].priority;
                curr = child + 1;
            }
        }
        if(child +  2 < size){
            if(data[child + 2].priority < small){
                small = data[child + 2].priority;
                curr = child + 2;
            }
        }
        if(child +  3 < size){
            if(data[child + 3].priority < small){
                small = data[child + 3].priority;
                curr = child + 3;
            }
        }
        if(child +  4 < size){
            if(data[child + 4].priority < small){
                small = data[child + 4].priority;
                curr = child + 4;
            }
        }
        if(curr != idx){
            PQElement<E> temp = data[idx];
            data[idx] = data[curr];
            keyToIndexMap.put(data[idx].data, idx);
            data[curr] = temp;
            keyToIndexMap.put(data[curr].data, curr);
            percolateDown(curr);
        }
    }

    @Override
    public void increaseKey(PQElement<E> key) {
        if(!keyToIndexMap.containsKey(key.data)){
            throw new IllegalArgumentException("Doesn't exist");
        }
        data[keyToIndexMap.get(key.data)] = key;
        percolateDown(keyToIndexMap.get(key.data));
    }

    @Override
    public void decreaseKey(PQElement<E> key) {
        if(!keyToIndexMap.containsKey(key.data)){
            throw new IllegalArgumentException("Doesn't exist");
        }
        data[keyToIndexMap.get(key.data)] = key;
        percolateUp(keyToIndexMap.get(key.data));
    }

    @Override
    public boolean enqueue(PQElement<E> epqElement) {
        if(keyToIndexMap.containsKey(epqElement.data)){
            throw new IllegalArgumentException("Duplicate");
        }
        if(size >= data.length){
            PQElement<E>[] temp = new  PQElement[data.length*2];
            for(int i = 0; i < data.length;i++){
                temp[i] = data[i];
            }
            data = temp;
        }
        data[size] = epqElement;
        keyToIndexMap.put(epqElement.data, size);
        percolateUp(size);
        size++;
        return true;
    }

    @Override
    public PQElement<E> dequeue() {
        if (size <= 0){
            return null;
        }
        PQElement<E> temp = data[0];
        data[0] = data[size - 1];
        percolateDown(0);
        size--;
        return temp;
    }

    @Override
    public PQElement<E> peek() {
        if(size != 0){
            return data[size - 1];
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<PQElement<E>> iterator() {
        return null;
    }

}