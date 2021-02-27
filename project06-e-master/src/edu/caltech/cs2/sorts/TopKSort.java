package edu.caltech.cs2.sorts;

import edu.caltech.cs2.datastructures.MinFourHeap;
import edu.caltech.cs2.interfaces.IPriorityQueue;

public class TopKSort {
    /**
     * Sorts the largest K elements in the array in descending order.
     * @param array - the array to be sorted; will be manipulated.
     * @param K - the number of values to sort
     * @param <E> - the type of values in the array
     */
    public static <E> void sort(IPriorityQueue.PQElement<E>[] array, int K) {
        if (K < 0) {
            throw new IllegalArgumentException("K cannot be negative!");
        }
        MinFourHeap<E> heap = new MinFourHeap<>();
        for(int i = 0; i < array.length; i++){
            if(i < array.length) {
                heap.enqueue(array[i]);
            }
        }
        for(int i = 0; i < array.length; i++){
            if (array.length - 1 - i < K) {
                array[array.length - 1 - i] = heap.dequeue();
            }
            else{
                heap.dequeue();
                array[array.length - 1 - i] = null;
            }
        }
    }
}
