package com.luxoft.olshevchenko.list;

/**
 * @author Oleksandr Shevchenko
 */
class LinkedListTest extends ListTest {
    @Override
    protected List<String> getList() {
        return new LinkedList<>();
    }
}