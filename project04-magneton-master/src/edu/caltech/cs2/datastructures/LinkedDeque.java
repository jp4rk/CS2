package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

    private Node<E> tail;
    private Node<E> head;
    private int size;

    private static class Node<E>{
        public final E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(E data) {
            this(data, null, null);
        }

        public Node(E data, Node<E> next, Node<E> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    public LinkedDeque(){
        this.size = 0;
    }


    @Override
    public void addFront(E e) {
        if (size == 0){
            this.head = new Node<>(e);
            this.tail = this.head;
        }
        else {
            this.head = new Node<>(e, this.head, null);
            this.head.next.prev = this.head;
        }
        size++;
    }

    @Override
    public void addBack(E e) {
        if (size == 0){
            this.head = new Node<>(e);
            this.tail = this.head;
        }
        else {
            this.tail = new Node<>(e, null, this.tail);
            this.tail.prev.next = this.tail;
        }
        size++;
    }

    @Override
    public E removeFront() {
        if (size <= 0) return null;
        E data = this.head.data;
        this.head = this.head.next;
        if (this.head == null){
            this.tail = null;
        }
        else {
            this.head.prev = null;
        }
        size--;
        return data;
    }

    @Override
    public E removeBack() {
        if (size <= 0) return null;
        E data = this.tail.data;
        this.tail = this.tail.prev;
        if (this.tail == null){
            this.head = null;
        }
        else {
            this.tail.next = null;
        }
        size--;
        return data;
    }

    @Override
    public boolean enqueue(E e) {
        addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        return removeBack();
    }

    @Override
    public boolean push(E e) {
        addBack(e);
        return true;
    }

    @Override
    public E pop() {
        return removeBack();
    }

    @Override
    public E peek() {
        if (this.tail == null){
            return null;
        }
        return this.tail.data;
    }

    @Override
    public E peekFront() {
        if (this.head == null){
            return null;
        }
        return this.head.data;
    }

    @Override
    public E peekBack() {
        if (this.tail == null){
            return null;
        }
        return this.tail.data;
    }

    public class LinkedDequeIterator implements Iterator<E> {
        private Node<E> idx;

        public LinkedDequeIterator(){
            this.idx = LinkedDeque.this.head;
        }

        @Override
        public boolean hasNext() {
            return this.idx != null;
        }

        @Override
        public E next() {
            E e = this.idx.data;
            this.idx = this.idx.next;
            return e;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedDequeIterator();
    }

    @Override
    public int size() {
        return size;
    }

    public String toString(){
        String ret = "[";
        Node<E> temp = this.head;
        if (this.head == null){
            return "[]";
        }
        for (int i = 0; i < size; i++){
            ret += temp.data + ", ";
            temp = temp.next;
        }
        return ret.substring(0, ret.length() - 2) + "]";
    }

}
