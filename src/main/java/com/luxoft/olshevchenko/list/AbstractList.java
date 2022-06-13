package com.luxoft.olshevchenko.list;

import java.util.StringJoiner;

/**
 * @author Oleksandr Shevchenko
 */
public abstract class AbstractList<E> implements List<E> {
    private static final String INDEX_OOB_MSG_FORMAT = "Index %s is out of bounds in [0, %s]";
    protected int size;

    @Override
    public void add(E value) {
        add(value, size);
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
    public String toString(){
        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
        for (E e : this) {
            stringJoiner.add(e.toString());
        }
        return stringJoiner.toString();
    }

    protected void checkExceedBoundsForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(String.format(INDEX_OOB_MSG_FORMAT, index, size));
        }
    }

    protected void checkExceedBoundsForRemoveGetSet(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.format(INDEX_OOB_MSG_FORMAT, index, size));
        }
    }


}
