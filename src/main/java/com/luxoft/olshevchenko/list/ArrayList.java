package com.luxoft.olshevchenko.list;

import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
public class ArrayList<E> extends AbstractList<E> implements List<E> {
    private final static int DEFAULT_CAPACITY = 10;
    private final static double LOAD_FACTOR = 1.5;
    private E [] array;

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ArrayList(int capacity) {
        this.array = (E[]) new Object[capacity];
    }


    @Override
    public void add(E value, int index) {
        checkExceedBoundsForAdd(index);
        ensureCapacity();
        if (value != null) {
            System.arraycopy(array, index, array, index + 1, size - index);
            array[index] = value;
            size++;
        }
    }

    @Override
    public E remove(int index) {
        checkExceedBoundsForRemoveGetSet(index);
        E result = array[index];
        System.arraycopy(array, index + 1, array, index, size() - index - 1);
        size--;
        return result;
    }

    @Override
    public E get(int index) {
        checkExceedBoundsForRemoveGetSet(index);
        return array[index];
    }

    @Override
    public E set(E value, int index) {
        checkExceedBoundsForRemoveGetSet(index);
        E result = array[index];
        array[index] = value;
        return result;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(E value) {
        for (int i = 0; i < size; i++) {
            if (array[i] != null && array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(E value) {
        for (int i = size - 1; i >= 0; i--) {
            if (array[i] != null && array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }


    private void ensureCapacity() {
        if (size == array.length) {
            array = Arrays.copyOf(array, Math.max((int) (array.length * LOAD_FACTOR), 2));
        }
    }


    @Override
    public Iterator<E> iterator() {
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements Iterator<E> {
        private int index;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There is no next element in the list");
            }
            return array[index++];
        }

        @Override
        public void remove() {
            if (index == 0) {
                throw new IllegalStateException("Called remove method without next");
            }
            ArrayList.this.remove(index - 1);
        }


    }


}
