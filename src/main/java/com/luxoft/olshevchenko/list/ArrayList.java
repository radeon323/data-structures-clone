package com.luxoft.olshevchenko.list;

import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
public class ArrayList<E> implements List<E> {

    private final static double LOAD_FACTOR = 1.5;
    private final static int DEFAULT_CAPACITY = 10;
    private int size = 0;
    private E [] array;

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ArrayList(int capacity) {
        this.array = (E[]) new Object[capacity];
    }

    @Override
    public void add(E value) {
        add(value, size);
    }

    @Override
    public void add(E value, int index) {
        if (size == array.length) {
            ensureCapacity();
        }
        if (index <= size && index >= 0) {
            System.arraycopy(array, index, array, index + 1, size - index);
            array[index] = value;
            size++;
        } else {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
    }

    @Override
    public E remove(int index) {
        if (index < size && index >= 0) {
            E result = array[index];
            for (int i = 0; i < size; i++) {
                if (array[i].equals(array[index])){
                    array[i] = array[i+1];
                }
            }
            size--;
            return result;
        } else {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
    }

    @Override
    public E get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
        if (!isEmpty()) {
            return array[index];
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public E set(E value, int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
        if (!isEmpty()) {
            array[index] = value;
            return array[index];
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E value) {
        return indexOf(value) != -1;
    }

    @Override
    public int indexOf(E value) {
        if (!isEmpty()) {
            for (int i = 0; i < size; i++) {
                if (array[i].equals(value)) {
                    return i;
                }
            }
            return -1;
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public int lastIndexOf(E value) {
        if (!isEmpty()) {
            for (int i = size-1; i >= 0; i--) {
                if (array[i].equals(value)) {
                    return i;
                }
            }
            return -1;
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public String toString(){
        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
        for (int i = 0; i < size; i++) {
            stringJoiner.add(array[i].toString());
        }
        return stringJoiner.toString();
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        E [] tempArray = (E[]) new Object[(int) (array.length * LOAD_FACTOR)];
        System.arraycopy(array, 0, tempArray, 0, array.length);
        trimToSize();
        array = tempArray;
    }

    public void trimToSize() {
        array = Arrays.copyOf(array, size);
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<E> {
        private int index;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public E next() {
            if (hasNext()) {
                return array[index++];
            } else {
                throw new NoSuchElementException("There is no next element in the list");
            }
        }

        @Override
        public void remove() {
            if (index != 0) {
                ArrayList.this.remove(index - 1);
            } else {
                throw new IllegalStateException("Called remove method without next");
            }
        }
    }
}
