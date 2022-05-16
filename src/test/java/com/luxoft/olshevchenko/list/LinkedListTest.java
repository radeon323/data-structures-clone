package com.luxoft.olshevchenko.list;

class LinkedListTest extends ListTest {
    @Override
    protected List<String> getList() {
        return new LinkedList<>();
    }
}