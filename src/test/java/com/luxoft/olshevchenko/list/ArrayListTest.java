package com.luxoft.olshevchenko.list;

class ArrayListTest extends ListTest {
    @Override
    protected List<String> getList() {
        return new ArrayList<>();
    }
}