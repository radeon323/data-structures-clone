package com.luxoft.olshevchenko.map;

/**
 * @author Oleksandr Shevchenko
 */
public class HashMapTest extends MapTest{
    @Override
    protected Map<String, Integer> getMap() {
        return new HashMap<>();
    }
}
