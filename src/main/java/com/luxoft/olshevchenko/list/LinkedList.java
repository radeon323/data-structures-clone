package com.luxoft.olshevchenko.list;

import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
public class LinkedList<E> extends AbstractList<E> implements List<E>{
    private Node<E> head;
    private Node<E> tail;


    @Override
    public void add(E value, int index) {
        checkExceedBoundsForAdd(index);
        Node<E> newNode = new Node<>(value);
        if (value != null) {
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
                Node<E> prev = getNode(index - 1);
                prev.next.prev = newNode;
                newNode.next = prev.next;
                newNode.prev = prev;
                prev.next = newNode;
            }
                size++;
        }
    }

    @Override
    public E remove(int index) {
        checkExceedBoundsForRemoveGetSet(index);
        Node<E> currentNode = getNode(index);
        removeNode(currentNode);
        return currentNode.value;
    }

    @Override
    public E get(int index) {
        checkExceedBoundsForRemoveGetSet(index);
        Node<E> result = getNode(index);
        return result.value;
    }

    @Override
    public E set(E value, int index) {
        checkExceedBoundsForRemoveGetSet(index);
        Node<E> result = getNode(index);
        result.value = value;
        return result.value;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(E value) {
        Node<E> currentNode = head;
        for (int i = 0; i < size; i++) {
            if (currentNode != null && currentNode.value.equals(value)) {
                return i;
            }
            currentNode = Objects.requireNonNull(currentNode).next;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(E value) {
        Node<E> currentNode = tail;
        for (int i = size - 1; i >= 0; i--) {
            if (currentNode != null && currentNode.value.equals(value)) {
                return i;
            }
            currentNode = Objects.requireNonNull(currentNode).prev;
        }
        return -1;
    }


    private Node<E> getNode(int index) {
        Node<E> currentNode = head;
        if (index < size / 2){
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.next;
            }
        }
        currentNode = tail;
        for (int i = size - 1; i > index; i--) {
            currentNode = currentNode.prev;
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
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<E> {
        private Node<E> currentNode = head;
        private Node<E> nodeToRemove;

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There is no next element in the list");
            }
            E value = currentNode.value;
            nodeToRemove = currentNode;
            currentNode = currentNode.next;
            return value;
        }

        @Override
        public void remove() {
            if (nodeToRemove == null) {
                throw new IllegalStateException("Called remove method without next");
            }
            removeNode(nodeToRemove);
        }

    }

    private static class Node<E> {
        private Node<E> next;
        private Node<E> prev;
        private E value;

        private Node(E value) {
            this.value = value;
        }

    }


}
