package com.luxoft.olshevchenko.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
public abstract class MapTest {
    private Map<String, Integer> map;

    @BeforeEach
    public void before() {
        map = getMap();
    }

    protected abstract Map<String, Integer> getMap();
    private final java.util.Map<String, Integer> originalMap = new HashMap<>();


    @Test
    @DisplayName("testPutInTheSameBucket")
    void testPutInTheSameBucket() {
        map.put("AaAaAa", 1);
        map.put("AaAaBB", 2);
        map.put("AaBBAa", 3);
        map.put("AaBBBB", 4);
        map.put("BBAaAa", 5);
        map.put("BBAaBB", 6);
        map.put("BBBBAa", 7);
        map.put("BBBBBB", 8);

        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);
        map.put("E", 5);
        map.put("F", 6);
        map.put("G", 7);
        map.put("H", 8);
        assertEquals(1, map.get("AaAaAa"));
        assertEquals(2, map.get("AaAaBB"));
        assertEquals(3, map.get("AaBBAa"));
        assertEquals(4, map.get("AaBBBB"));
        assertEquals(5, map.get("BBAaAa"));
        assertEquals(6, map.get("BBAaBB"));
        assertEquals(7, map.get("BBBBAa"));
        assertEquals(8, map.get("BBBBBB"));
        assertEquals(16, map.size());
    }

    @Test
    @DisplayName("Test Put method and check size")
    void testPutAndCheckSize() {
        map.put("B", 1);
        map.put("G", 2);
        map.put("AaAa", 3);
        map.put("BBBB", 4);
        map.put("AaBB", 5);
        assertEquals(5, map.size());
    }

    @Test
    @DisplayName("Test Put method and grow and check size")
    void testPutAndGrowAndCheckSize() {
        map.put("B", 1);
        map.put("G", 2);
        map.put("AaAa", 3);
        map.put("BBBB", 4);
        map.put("AaBB", 5);
        assertEquals(5, map.size());
        map.put("BBAa", 6);
        map.put("AaAaAa", 7);
        map.put("AaAaBB", 8);
        map.put("AaBBAa", 9);
        map.put("AaBBBB", 10);
        assertEquals(10, map.size());
    }

    @Test
    @DisplayName("Test Put method in the same bucket and check Get if this element correct")
    void testPutInTheSameBucketAndCheckGetIfThisElementCorrect() {
        map.put("B", 1);
        map.put("G", 2);
        map.put("AaAa", 3);
        map.put("BBBB", 4);
        map.put("AaBB", 5);
        map.put("BBAa", 6);
        System.out.println(map);
        assertEquals(3, map.get("AaAa"));
        assertEquals(1, map.get("B"));
        assertEquals(2, map.get("G"));
        assertEquals(3, map.get("AaAa"));
        assertEquals(4, map.get("BBBB"));
        assertEquals(5, map.get("AaBB"));
        assertEquals(6, map.get("BBAa"));
        assertEquals(6, map.size());
    }

    @Test
    @DisplayName("Test Put method and check old value")
    void testPutAndCheckOldValue() {
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        assertEquals(1, map.put("A", 4));
        assertEquals(4, map.put("A", 5));
        assertEquals(2, map.put("B", 0));
        assertEquals(0, map.put("B", 10));
        assertNull(map.put("D", 5));
    }

    @Test
    @DisplayName("Test Put method in the same bucket differ values and check Get if this element correct")
    void testPutInTheSameBucketDifferValuesAndCheckGetIfThisElementCorrect() {
        map.put("A", 1);
        map.put("A", 2);
        map.put("A", 3);
        map.put("AaAa", 4);
        map.put("BBBB", 5);
        map.put("AaBB", 6);
        map.put("BBAa", 7);
        assertEquals(3, map.get("A"));
        assertEquals(4, map.get("AaAa"));
        assertEquals(5, map.get("BBBB"));
        assertEquals(6, map.get("AaBB"));
        assertEquals(7, map.get("BBAa"));
        assertEquals(5, map.size());
    }

    @Test
    @DisplayName("Test Get method if bucket is empty should return null")
    void testGetIfBucketIsEmpty() {
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        assertNull(map.get("D"));
    }

    @Test
    @DisplayName("Test Contains method")
    void testContains() {
        map.put("B", 1);
        map.put("G", 2);
        map.put("AaAa", 3);
        map.put("BBBB", 4);
        map.put("AaBB", 5);
        map.put("BBAa", 6);
        assertTrue(map.containsKey("B"));
        assertTrue(map.containsKey("G"));
        assertTrue(map.containsKey("AaAa"));
        assertTrue(map.containsKey("BBBB"));
        assertTrue(map.containsKey("AaBB"));
        assertTrue(map.containsKey("BBAa"));
        assertFalse(map.containsKey("C"));
    }

    @Test
    @DisplayName("Test Remove method and check removed value")
    void testRemove() {
        map.put("B", 2);
        map.put("G", 3);
        map.put("AaAa", 4);
        map.put("BBBB", 5);
        map.put("AaBB", 6);
        assertEquals(5, map.remove("BBBB"));
        assertNull(map.get("BBBB"));
        assertEquals(2, map.get("B"));
        assertEquals(3, map.get("G"));
        assertEquals(4, map.get("AaAa"));
        assertEquals(6, map.get("AaBB"));
        assertNull(map.get("BBBB"));
        assertEquals(4, map.size());
    }

    @Test
    @DisplayName("Test Remove method if bucket is empty")
    void testRemoveIfBucketIsEmpty() {
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        Assertions.assertThrows(IllegalStateException.class, () -> map.remove("D"));
    }

    @Test
    @DisplayName("Test Size method on empty map")
    void testSizeOnEmptyMap() {
        assertEquals(0, map.size());
    }

    @Test
    @DisplayName("Test Iterator Next method and check size")
    void testIteratorNextAndCheckSize() {
        Iterator<Map.Entry<String, Integer>> iterator = map.iterator();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        int counter = 0;
        while (iterator.hasNext()) {
            counter++;
            iterator.next();
        }
        assertEquals(map.size(), counter);
    }

    @Test
    @DisplayName("Test Iterator call Remove method without Next")
    void testIteratorCallRemoveWithoutNext() {
        Iterator<Map.Entry<String, Integer>> iterator = map.iterator();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        assertThrows(IllegalStateException.class, () -> {
            while (iterator.hasNext()) {
                iterator.remove();
            }
        });
    }

    @Test
    @DisplayName("Test Iterator Next method and Get after Remove")
    void testIteratorNextAndGetAfterRemove() {
        Iterator<Map.Entry<String, Integer>> iterator = map.iterator();
        map.put("A", 1);
        map.put("A", 2);
        map.put("A", 3);
        map.put("AaAa", 4);
        map.put("BBBB", 5);
        map.put("AaBB", 6);
        map.put("BBAa", 7);
        assertEquals(5, map.size());
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.remove();
        assertNull(map.get("BBBB"));
        assertEquals(3, map.get("A"));
        assertEquals(4, map.get("AaAa"));
        assertEquals(6, map.get("AaBB"));
        assertEquals(7, map.get("BBAa"));
        assertNull(map.get("BBBB"));
        assertEquals(4, map.size());
        assertNull(map.get("C"));
    }

    @Test
    @DisplayName("Test Iterator Next method when no next element")
    void testIteratorNextWhenNoNextElement() {
        Iterator<Map.Entry<String, Integer>> iterator = map.iterator();
        map.put("A", 3);
        map.put("AaAa", 4);
        map.put("BBBB", 5);
        assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
            iterator.next();
            iterator.next();
            iterator.next();
        });
    }


}