package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

    private E[] array;
    private int size;
    private static final int capacity = 10;
    private static final int multiple = 2;

    public ArrayDeque() {
        this(capacity);
    }

    public ArrayDeque(int initialCapacity) {
        this.array = (E[]) new Object[initialCapacity];
        this.size = 0;
    }

    @Override
    public void addFront(E e) {
        if (this.size + 1 > this.array.length) {
            E[] arrayCopy = (E[]) new Object[this.array.length * multiple];
                for(int i = 0; i < this.array.length; i++) {
                    arrayCopy[i] = this.array[i];
                }

                this.array = arrayCopy;
            }

            for (int i = this.size; i >0; i--) {
                this.array[i] = this.array[i -1];

            }
        this.array[0] = e;
        this.size ++;
        }

    @Override
    public void addBack(E e) {
        if(this.size + 1 > this.array.length) {
            E[] arrayCopy = (E[]) new Object[this.array.length * multiple];
            for (int k = 0; k < this.array.length; k++) {
                arrayCopy[k] = this.array[k];
            }
            this.array = arrayCopy;
        }
        this.array[this.size] = e;
        this.size ++;
    }

    @Override
    public E removeFront() {

        if (this.size <= 0) {
            return null;
        }

        E ans = this.array[0];
        for(int k = 0; k < this.size - 1; k++) {
            this.array[k] = this.array[k+1];
        }
        this.size--;
        return ans;
    }

    @Override
    public E removeBack() {
        if (this.size <= 0) {
            return null;
        }
        this.size--;
        return this.array[this.size];
    }

    @Override
    public boolean enqueue(E e) {
        addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        if (this.size <= 0) {
            return null;
        }
        this.size --;
        return this.array[this.size];
    }

    @Override
    public boolean push(E e) {
        addBack(e);
        return true;
    }

    @Override
    public E pop() {
        if (this.size <= 0) {
            return null;
        }
        this.size--;
        return this.array[this.size];
    }

    @Override
    public E peek() {
        if (this.size <= 0) {
            return null;
        }
        return this.array[this.size - 1];
    }

    @Override
    public E peekFront() {
        if (this.size <= 0) {
            return null;
        }
        return this.array[0];
    }

    @Override
    public E peekBack() {
        if (this.size <= 0) {
            return null;
        }

        return this.array[this.size - 1];
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public int size() {
        return this.size;
    }

    public class ArrayDequeIterator implements Iterator<E> {
        private int index;

        public ArrayDequeIterator() {
            this.index = 0;
        }

        public boolean hasNext() {
            return this.index < ArrayDeque.this.size;
        }

        public E next() {
            E e = ArrayDeque.this.array[this.index];
            this.index++;
            return e;
        }

    }

    public String toString() {
        String ans = "[";
        for (int i = 0; i < this.size ; i++) {
            ans += this.array[i] + ", ";
        }
        if (ans.length() < 2) {
            return "[]";
        }
        return ans.substring(0, ans.length() - 2) + "]";
    }

}

