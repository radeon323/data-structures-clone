package com.luxoft.olshevchenko.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayListTest {

    private final ArrayList<String> list = new ArrayList<>();

    @Test
    void testAddAndCapacityIncrease() {
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        list.add("G");
        list.add("H");
        list.add("I");
        list.add("J");
        list.add("K");
        list.add("L");
        list.add("M");
        assertEquals(13, list.size());
    }

    @Test
    void testAddIfIndexInBounds() {
        list.add("A",0);
        list.add("B",1);
        list.add("C",2);
        list.add("D",2);
        assertEquals("C", list.remove(3));
        assertEquals("D", list.remove(2));
        assertEquals("B", list.remove(1));
        assertEquals("A", list.remove(0));
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void testAddIfIndexOutOfBounds() {
        list.add("A");
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            list.add("B",2);
        });
    }

    @Test
    void testRemoveIfIndexInBounds() {
        list.add("A");
        list.add("B");
        list.add("C");
        assertEquals("C", list.remove(2));
        assertEquals("B", list.remove(1));
        assertEquals("A", list.remove(0));
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void testRemoveIfIndexOutOfBounds() {
        list.add("A");
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            list.remove(1);
        });
    }

    @Test
    void testGetIfIndexInBounds() {
        list.add("A");
        list.add("B");
        list.add("C");
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
        assertEquals(3, list.size());
    }

    @Test
    void testGetIfIndexOutOfBounds() {
        list.add("A");
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            list.get(1);
        });
    }

    @Test
    void testSetIfIndexInBounds() {
        list.add("A");
        list.add("B");
        list.add("C");
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
        list.set("D", 0);
        assertEquals("D", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    void testSetIfIndexOutOfBounds() {
        list.add("A");
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            list.set("D", 1);
        });
    }

    @Test
    void testClear() {
        list.add("A");
        list.add("B");
        list.add("C");
        list.clear();
        assertTrue(list.isEmpty());
    }

    @Test
    void testSizeChange() {
        list.add("A");
        list.add("B");
        list.add("C");
        assertEquals(3, list.size());
        list.add("D",3);
        assertEquals(4, list.size());
        list.add("E",1);
        assertEquals(5, list.size());
        assertNull(list.get(4));
    }

    @Test
    void testIsEmpty() {
        list.add("A");
        assertFalse(list.isEmpty());
    }

    @Test
    void contains() {
        list.add("A");
        list.add("B");
        assertTrue(list.contains("A"));
        assertFalse(list.contains("C"));
    }

    @Test
    public void testIndexOf(){
        list.add("A");
        list.add("B");
        list.add("A");
        list.add("C");
        assertEquals(0 , list.indexOf("A"));
    }

    @Test
    public void testLastIndexOf(){
        list.add("A");
        list.add("B");
        list.add("A");
        list.add("C");
        assertEquals(2 , list.lastIndexOf("A"));
    }

    @Test
    public void testToString(){
        list.add("A");
        list.add("B");
        list.add("C");
        assertEquals("[A, B, C]", list.toString());
    }
}