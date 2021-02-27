package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
    private E [] data;
    private int size;
    private static final int capicity = 10;
    private static final int factor = 2;

    public ArrayDeque(){
        this(capicity);
    }

    public ArrayDeque(int initialCapacity){
        this.data = (E[]) new Object[initialCapacity];
        this.size = 0;
    }

    @Override
    public void addFront(E e) {
        if(size + 1 > data.length){
            E [] copy = (E[]) new Object[data.length * factor];
            for(int i = 0; i < data.length; i++){
                copy[i] = data[i];
            }
            data = copy;
        }

        for (int i = size ; i > 0; i--){
            data[i] = data[i - 1];
        }
        data[0] = e;
        size++;
    }

    @Override
    public void addBack(E e) {
        if(size + 1 > data.length){
            E [] copy = (E[]) new Object[data.length * factor];
            for(int i = 0; i < data.length; i++){
                copy[i] = data[i];
            }
            data = copy;
        }
        data[size] = e;
        size++;
    }

    @Override
    public E removeFront() {
        if (size <= 0) return null;
        E ret = data[0];
        for (int i = 0; i < size - 1; i++){
            data[i] = data[i + 1];
        }
        size--;
        return ret;
    }
    @Override
    public E removeBack() {
        if (size <= 0) return null;
        size--;
        return data[size];
    }

    @Override
    public boolean enqueue(E e) {
        addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        if (size <=0) return null;
        size--;
        return data[size];
    }

    @Override
    public boolean push(E e) {
        addBack(e);
        return true;
    }

    @Override
    public E pop() {
        if (size <= 0) return null;
        size--;
        return data[size];
    }

    @Override
    public E peek() {
        if (size <= 0) return null;
        return data[size - 1];
    }

    @Override
    public E peekFront() {
        if (size <= 0) return null;
        return data[0];
    }

    @Override
    public E peekBack() {
        if (size <= 0) return null;
        return data[size - 1];
    }

    public class ArrayDequeIterator implements Iterator<E> {
        private int idx;

        public ArrayDequeIterator(){
            this.idx = 0;
        }

        @Override
        public boolean hasNext() {
            return this.idx < ArrayDeque.this.size;
        }

        @Override
        public E next() {
            E e = ArrayDeque.this.data[this.idx];
            this.idx++;
            return e;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public int size() {
        return this.size;
    }


    @Override
    public String toString(){
        String ret = "[";
        for (int i = 0; i < size; i++){
            ret += data[i] + ", ";
        }
        if (ret.length() < 2) return "[]";
        return ret.substring(0,ret.length() - 2) + "]";
    }

}

