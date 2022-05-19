package com.luxoft.olshevchenko.list;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Oleksandr Shevchenko
 */
public class LinkedList<E> implements List<E>{
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;


    @Override
    public void add(E value) {
        add(value, size);
    }

    @Override
    public void add(E value, int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
        Node<E> newNode = new Node<>(value);
        if (head == null) {
            head = tail = newNode;
        } else if (index == 0) {
            head.prev = newNode;
            newNode.next = head;
            head = newNode;
        } else if (index == size) {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            tail.next = null;
        } else {
            Node<E> prev = getNodeByIndex(index - 1);
            prev.next.prev = newNode;
            newNode.next = prev.next;
            newNode.prev = prev;
            prev.next = newNode;
        }
        size++;
    }

    @Override
    public E remove(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
        Node<E> currentNode = head;
        if (size == 1) {
            head = tail = null;
        } else if (index == size - 1) {
            currentNode = tail;
            tail = tail.prev;
            tail.next = null;
        } else if (index == 0) {
            head = head.next;
            head.prev = null;
        } else {
            currentNode = getNodeByIndex(index);
            currentNode.prev.next = currentNode.next;
            currentNode.next.prev = currentNode.prev;
        }
        size--;
        return currentNode.value;
    }

    @Override
    public E get(int index) {
        if (!isEmpty()) {
            Objects.checkIndex(index, size);
            Node<E> result = getNodeByIndex(index);
            return result.value;
        } else if (index >= size) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public E set(E value, int index) {
        if (!isEmpty()) {
            Objects.checkIndex(index, size);
            Node<E> result = getNodeByIndex(index);
            result.value = value;
            return result.value;
        } else if (index >= size) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public void clear() {
        head = tail = null;
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
            Node<E> currentNode = head;
            for (int i = 0; i < size; i++) {
                if (Objects.equals(currentNode.value, value)) {
                    return i;
                }
                currentNode = currentNode.next;
            }
            return -1;
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public int lastIndexOf(E value) {
        if (!isEmpty()) {
            Node<E> current = tail;
            for (int i = size - 1; i >= 0; i--) {
                if (Objects.equals(current.value, value)) {
                    return i;
                }
                current = current.prev;
            }
            return -1;
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
        Node<E> current = head;
        while (current != null) {
            stringJoiner.add(current.value.toString());
            current = current.next;
        }
        return stringJoiner.toString();
    }

    private Node<E> getNodeByIndex(int index) {
        Node<E> currentNode = head;
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.next;
            }
        return currentNode;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<E> {
        private Node<E> currentNode;
        private Node<E> nextNode = head;

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public E next() {
            if (hasNext()) {
                E value = nextNode.value;
                currentNode = nextNode;
                nextNode = nextNode.next;
                return value;
            } else {
                throw new NoSuchElementException("The element does not exist");
            }
        }

        @Override
        public void remove() {
            if (currentNode == null) {
                throw new IllegalStateException("The element to remove is not identified");
            } else {
                if (currentNode == head) {
                    head = currentNode.next;
                    head.prev = null;
                } else if (currentNode == tail) {
                    tail = currentNode.prev;
                    tail.next = null;
                } else {
                    currentNode.prev.next = currentNode.next;
                    currentNode.next.prev = currentNode.prev;
                }
            }
            size--;
        }
    }

    private static class Node<E> {
        private E value;
        private Node<E> next;
        private Node<E> prev;

        public Node(E value) {
            this.value = value;
        }


    }

}
