package com.luxoft.olshevchenko.list;

/**
 * @author Oleksandr Shevchenko
 */
class ArrayListTest extends ListTest {
    @Override
    protected List<String> getList() {
        return new ArrayList<>();
    }
}