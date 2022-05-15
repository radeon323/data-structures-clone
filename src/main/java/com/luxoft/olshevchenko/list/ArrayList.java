package com.luxoft.olshevchenko.list;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Oleksandr Shevchenko
 */
public class ArrayList<E> implements List<E> {

    private final static double LOAD_FACTOR = 1.5;
    private final static int DEFAULT_CAPACITY = 10;
    private int size = 0;
    private Object[] list;

    public ArrayList() {
        this.list = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public void add(E value) {
        add(value, size);
    }

    @Override
    public void add(E value, int index) {
        if (list.length == size + 1) {
            Object[] tempArray = new Object[(int) (list.length * LOAD_FACTOR)];
            System.arraycopy(list, 0, tempArray, 0, size);
            list = tempArray;
        }
        if (index <= size) {
            list[index + 1] = list[index];
            list[index] = value;
            size++;
        } else {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
    }

    @Override
    public E remove(int index) {
        if (index < size) {
            E result = (E) list[index];
            for (int i = 0; i < size; i++) {
                if (Objects.equals(list[i],list[index])){
                    list[i] = list[i+1];
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
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
        if (!isEmpty()) {
            return (E) list[index];
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public E set(E value, int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
        if (!isEmpty()) {
            list[index] = value;
            return (E) list[index];
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            list[i] = null;
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
        int index = -1;
        if (!isEmpty()) {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(list[i], value)) {
                    index = i;
                    break;
                }
            }
            return index;
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public int lastIndexOf(E value) {
        int index = -1;
        if (!isEmpty()) {
            for (int i = size-1; i >= 0; i--) {
                if (Objects.equals(list[i], value)) {
                    index = i;
                    break;
                }
            }
            return index;
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public String toString(){
        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
        for (int i = 0; i < size; i++) {
            stringJoiner.add(list[i].toString());
        }
        return stringJoiner.toString();
    }

}
