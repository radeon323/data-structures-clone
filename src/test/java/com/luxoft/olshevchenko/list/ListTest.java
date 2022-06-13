package com.luxoft.olshevchenko.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Oleksandr Shevchenko
 */
public abstract class ListTest {
    private List<String> list;

    @BeforeEach
    public void before() {
        list = getList();
    }

    protected abstract List<String> getList();
    private final java.util.List<String> originalArrayList = new ArrayList<>();
    private final java.util.List<String> originalLinkedList = new LinkedList<>();

    @Test
    @DisplayName("Test Add method and capacity increase")
    void testAddAndCapacityIncrease() {
        for (int i = 0; i < 20; i++) {
            list.add(String.valueOf(i));
        }
        assertEquals(20, list.size());
    }

    @Test
    @DisplayName("Test Add null")
    void testAddNull() {
        list.add("A");
        list.add("B");
        list.add("C");
        list.add(null);
        list.add(null);
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("Test Add and Remove method if index does not exceed bounds")
    void testAddIfIndexInBounds() {
        list.add("A",0);
        list.add("B",1);
        list.add("C",2);
        list.add("D",2);
        list.add("E",2);
        assertEquals("D", list.remove(3));
        assertEquals("A", list.remove(0));
        assertEquals("B", list.remove(0));
        assertEquals("E", list.remove(0));
        assertEquals("C", list.remove(0));
        assertEquals(0, list.size());
        Assertions.assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Test Add method if index exceeds bounds")
    void testAddIfIndexOutOfBounds() {
        list.add("A");
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.add("B",2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.add("C",-1));
    }

    @Test
    @DisplayName("Test Remove method if index exceeds bounds")
    void testRemoveIfIndexOutOfBounds() {
        list.add("A");
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
    }

    @Test
    @DisplayName("Test Get method if index does not exceed bounds")
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
    @DisplayName("Test Get method if index exceeds bounds")
    void testGetIfIndexOutOfBounds() {
        list.add("A");
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    @DisplayName("Test Set method if index does not exceed bounds")
    void testSetIfIndexInBounds() {
        list.add("A");
        list.add("B");
        list.add("C");
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
        list.set("D", 0);
        assertEquals("D", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("Test Set method if index exceeds bounds")
    void testSetIfIndexOutOfBounds() {
        list.add("A");
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.set("D", 1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.set("H", -1));
    }

    @Test
    @DisplayName("Test Clear method")
    void testClear() {
        list.add("A");
        list.add("B");
        list.add("C");
        list.clear();
        Assertions.assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Test Size change method")
    void testSizeChange() {
        list.add("A");
        list.add("B");
        list.add("C");
        assertEquals(3, list.size());
        list.add("D",3);
        assertEquals(4, list.size());
        list.add("E",1);
        assertEquals(5, list.size());
        assertEquals("D", list.get(4));
    }

    @Test
    @DisplayName("Test IsEmpty method")
    void testIsEmpty() {
        Assertions.assertTrue(list.isEmpty());
        list.add("A");
        Assertions.assertFalse(list.isEmpty());
    }

    @Test
    @DisplayName("Test Contains method")
    void testContains() {
        list.add("A");
        list.add("B");
        Assertions.assertTrue(list.contains("A"));
        Assertions.assertFalse(list.contains("C"));
    }

    @Test
    @DisplayName("Test IndexOf method")
    public void testIndexOf(){
        list.add("A");
        list.add("B");
        list.add("A");
        list.add("C");
        assertEquals(0 , list.indexOf("A"));
        Assertions.assertNotEquals(2 , list.indexOf("A"));
    }

    @Test
    @DisplayName("Test LastIndexOf method")
    public void testLastIndexOf(){
        list.add("A");
        list.add("B");
        list.add("A");
        list.add("C");
        Assertions.assertNotEquals(0 , list.lastIndexOf("A"));
        assertEquals(2 , list.lastIndexOf("A"));
    }


    @Test
    @DisplayName("Test Iterator Next and Remove methods")
    void testIteratorNextAndRemove() {
        list.add("A");
        list.add("B");
        list.add("C");
        Iterator<String> iterator = list.iterator();
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        iterator.remove();
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    @DisplayName("Test Iterator Next method if the element does not exist")
    void testIteratorNextIfTheElementDoesNotExist() {
        list.add("A");
        Iterator<String> iterator = list.iterator();
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
            iterator.next();
        });
    }

    @Test
    @DisplayName("Test Iterator Remove method if the element does not exist")
    void testIteratorRemoveIfTheElementDoesNotExist() {
        list.add("A");
        Iterator<String> iterator = list.iterator();
        Assertions.assertThrows(IllegalStateException.class, iterator::remove);
    }
}
