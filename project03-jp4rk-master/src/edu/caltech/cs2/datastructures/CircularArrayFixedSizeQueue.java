package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IFixedSizeQueue;

import java.util.Iterator;

public class CircularArrayFixedSizeQueue<E> implements IFixedSizeQueue<E> {



    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int capacity() {
        return 0;
    }

    @Override
    public boolean enqueue(E e) {
        return false;
    }

    @Override
    public E dequeue() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }
}
