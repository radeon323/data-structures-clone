package com.luxoft.olshevchenko.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
    @DisplayName("Test Put method and check size")
    void testPutAndCheckSize() {
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        assertEquals(3, map.size());
    }

    @Test
    @DisplayName("Test Put method and grow and check size")
    void testPutAndGrowAndCheckSize() {
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);
        map.put("E", 5);
        assertEquals(5, map.size());
        map.put("F", 6);
        map.put("G", 7);
        System.out.println(map);
        assertEquals(7, map.size());

        originalMap.put("A", 1);
        originalMap.put("B", 2);
        originalMap.put("C", 3);
        originalMap.put("D", 4);
        originalMap.put("E", 5);
        assertEquals(5, originalMap.size());
        originalMap.put("F", 6);
        originalMap.put("G", 7);
        System.out.println(originalMap);
        assertEquals(7, originalMap.size());
    }

    @Test
    @DisplayName("Test Put method in the same bucket and check Get if this element correct")
    void testPutInTheSameBucketAndCheckGetIfThisElementCorrect() {
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("C", 4);
        assertEquals(3, map.size());
    }

    @Test
    @DisplayName("Test Put method and check old value")
    void testPutAndCheckOldValue() {
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        assertEquals(1, map.put("A", 4));
        assertEquals(4, map.put("A", 5));
    }

    @Test
    @DisplayName("Test Get method")
    void testGet() {
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        assertEquals(1, map.get("A"));
        assertEquals(2, map.get("B"));
        assertEquals(3, map.get("C"));
        assertEquals(3, map.size());
    }

    @Test
    @DisplayName("Test Get method if bucket is empty should return null")
    void testGetIfBucketIsEmpty() {
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        assertNull(originalMap.get("D"));
        assertNull(map.get("D"));
    }

    @Test
    @DisplayName("Test Contains method")
    void testContains() {
        map.put("A", 1);
        map.put("B", 2);
        assertTrue(map.containsKey("A"));
        assertFalse(map.containsKey("C"));
    }

    @Test
    @DisplayName("Test Remove method and check removed value")
    void testRemove() {
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        assertEquals(1, map.remove("A"));
        assertEquals(2, map.get("B"));
        assertEquals(3, map.get("C"));
        assertEquals(2, map.size());
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
        map.put("B", 2);
        map.put("C", 3);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.remove();
        assertNull(map.get("C"));
    }

    @Test
    @DisplayName("Test Iterator Next method and Get after Remove")
    void testIteratorNextWhenNoNextElement() {
        Iterator<Map.Entry<String, Integer>> iterator = map.iterator();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
            iterator.next();
            iterator.next();
            iterator.next();
        });
    }


}