package com.luxoft.olshevchenko.map;

import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
public class HashMap<K, V> implements Map<K, V> {
    private static final int INITIAL_CAPACITY = 5;
    private static final int GROW_CONST = 2;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] buckets;
    private int size;

    public HashMap() {
        this(INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public HashMap(int length) {
        buckets = new Entry[length];
    }


    @Override
    public V put(K key, V value) {
        resize();
        int index = getIndex(buckets, key);
        Entry<K,V> currentBucket = buckets[index];
        if (currentBucket == null) {
            buckets[index] = new Entry<>(key, value);
            size++;
            return null;
        } else {
            if (Objects.equals(currentBucket.key, key)) {
                return changeValueIfKeyEquals(currentBucket, value);
            }
            while (currentBucket != null) {
                if (Objects.equals(currentBucket.key, key)) {
                    return changeValueIfKeyEquals(currentBucket, value);
                }
                if (key != null && currentBucket.hash == key.hashCode()) {
                    return addEntryIntoTheEndOfTheChainAndIncreaseSize(buckets, key, value);
                }
                currentBucket = currentBucket.next;
            }
            return addEntryIntoTheEndOfTheChainAndIncreaseSize(buckets, key, value);
        }
    }

    @Override
    public V get(K key) {
        int index = getIndex(buckets, key);
        Entry<K,V> currentBucket = buckets[index];
        if (currentBucket != null) {
            if (Objects.equals(currentBucket.key, key)) {
                return currentBucket.value;
            } else {
                while (currentBucket != null) {
                    if (Objects.equals(currentBucket.key, key)) {
                        return currentBucket.value;
                    }
                    currentBucket = currentBucket.next;
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int index = getIndex(buckets, key);
        Entry<K,V> currentBucket = buckets[index];
        while (currentBucket != null) {
            if (Objects.equals(currentBucket.key, key)) {
                return true;
            }
            currentBucket = currentBucket.next;
        }
        return false;
    }

    @Override
    public V remove(K key) {
        V resultValue;
        int index = getIndex(buckets, key);
        Entry<K, V> currentBucket = buckets[index];
        if (currentBucket == null) {
            throw new IllegalStateException("The bucket corresponding to the key " + key + " is empty");
        } else {
            if (Objects.equals(currentBucket.key, key)) {
                resultValue = currentBucket.value;
                buckets[index] = null;
                size--;
                return resultValue;
            }
            while (true) {
                if (Objects.equals(currentBucket.next.key, key)) {
                    resultValue = currentBucket.next.value;
                    buckets[index].next = buckets[index].next.next;
                    size--;
                    return resultValue;
                }
                currentBucket = currentBucket.next;
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ", "{", "}");
        for (Map.Entry<K, V> entry : this) {
            stringJoiner.add(entry.toString());
        }
        return stringJoiner.toString();
    }


    private void resize() {
        if (Math.ceil(buckets.length * LOAD_FACTOR) == size) {
            HashMap<K, V> newMap = new HashMap<>(INITIAL_CAPACITY * GROW_CONST);
            for (Map.Entry<K, V> entry : this) {
                newMap.put(entry.getKey(), entry.getValue());
            }
            buckets = newMap.buckets;
        }
    }

    private int getIndex(Entry<K, V>[] buckets, K key) {
        if (key == null) {
            return 0;
        }
        if (key.hashCode() == Integer.MIN_VALUE) {
            return 0;
        }
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private Entry<K, V> getLastEntry(Entry<K, V> entry) {
        while(entry.next != null) {
            entry = entry.next;
        }
        return entry;
    }

    private V changeValueIfKeyEquals(Entry<K, V> currentBucket, V value) {
        V resultValue = currentBucket.value;
        currentBucket.value = value;
        return resultValue;
    }

    private V addEntryIntoTheEndOfTheChainAndIncreaseSize(Entry<K, V>[] buckets, K key, V value) {
        int index = getIndex(buckets, key);
        Entry<K, V> newEntry = new Entry<>(key, value);
        Entry<K, V> currentEntry = getLastEntry(buckets[index]);
        currentEntry.next = newEntry;
        size++;
        return currentEntry.value;
    }


    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<Map.Entry<K, V>> {
        private int index;
        private int entryCount;
        private boolean checkNext = false;
        private Entry<K, V> currentBucket;
        private Entry<K, V> currentEntry;

        @Override
        public boolean hasNext() {
            return entryCount != size;
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There is no next element in the map");
            }
            while (true) {
                currentBucket = buckets[index];
                if (currentEntry != null) {
                    currentBucket = currentEntry;
                }
                if (currentBucket == null) {
                    index++;
                } else {
                    if (currentBucket.next == null) {
                        index++;
                        entryCount++;
                        checkNext = true;
                        currentEntry = null;
                    } else {
                        entryCount++;
                        checkNext = true;
                        currentEntry = currentBucket.next;
                    }
                    return currentBucket;
                }
            }
        }

        @Override
        public void remove() {
            if (!checkNext) {
                throw new IllegalStateException("Called remove method without next");
            }
            HashMap.this.remove(currentBucket.key);
            checkNext = false;
        }
    }


    private static class Entry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = getHash(key);
            this.next = null;
        }


        @Override
        public K getKey() {
            return key;
        }

        @Override
        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        private int getHash(K key) {
            if (key == null) {
                return 0;
            }
            return key.hashCode();
        }


    }


}
