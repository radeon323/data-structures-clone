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
    private int size;


    @Override
    public void add(E value) {
        add(value, size);
    }

    @Override
    public void add(E value, int index) {
        if (index <= size && index >= 0) {
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
        } else {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
    }

    @Override
    public E remove(int index) {
        if (index < size && index >= 0) {
            Node<E> currentNode = getNodeByIndex(index);
            removeNode(currentNode);
            return currentNode.value;
        } else {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
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
                if (currentNode.value.equals(value)) {
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
            Node<E> currentNode = tail;
            for (int i = size - 1; i >= 0; i--) {
                if (currentNode.value.equals(value)) {
                    return i;
                }
                currentNode = currentNode.prev;
            }
            return -1;
        } else {
            throw new IllegalStateException("List is empty");
        }
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
        Node<E> currentNode = head;
        while (currentNode != null) {
            stringJoiner.add(currentNode.value.toString());
            currentNode = currentNode.next;
        }
        return stringJoiner.toString();
    }

    private Node<E> getNodeByIndex(int index) {
        Node<E> currentNode = head;
        if (index == size - 1){
            currentNode = tail;
        } else if (index <= size / 2){
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.next;
            }
        } else if (index > size / 2){
            for (int i = size - 1; i > index; i--) {
                currentNode = tail;
                currentNode = currentNode.prev;
            }
        }
        return currentNode;
    }

    private void removeNode(Node<E> nodeToRemove) {
        if (size == 1) {
            head = tail = null;
        } else if (nodeToRemove == head) {
            head = nodeToRemove.next;
            head.prev = null;
        } else if (nodeToRemove == tail) {
            tail = nodeToRemove.prev;
            tail.next = null;
        } else {
            nodeToRemove.prev.next = nodeToRemove.next;
            nodeToRemove.next.prev = nodeToRemove.prev;
        }
        size--;
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
                throw new NoSuchElementException("There is no next element in the list");
            }
        }

        @Override
        public void remove() {
            if (currentNode == null) {
                throw new IllegalStateException("Called remove method without next");
            } else {
                removeNode(currentNode);
            }
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
