package com.luxoft.olshevchenko.map;

import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
public class HashMap<K, V> implements Map<K, V> {
    private static final int INITIAL_CAPACITY = 5;
    private static final int GROW_CONST = 2;

    private Entry<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public HashMap() {
        buckets = new Entry[INITIAL_CAPACITY];
    }


    @Override
    public V put(K key, V value) {
        int index = getIndex(buckets, key);
        if (buckets.length == size) {
            resize();
        }
        if (buckets[index] == null) {
            buckets[index] = new Entry<>(key, value);
            size++;
            return null;
        } else if (buckets[index].key.equals(key)){
            while (buckets[index] != null) {
                if (buckets[index].key.equals(key) && !buckets[index].value.equals(value)) {
                    V resultValue = buckets[index].value;
                    buckets[index].value = value;
                    return resultValue;
                }
                buckets[index] = buckets[index].next;
            }
        } else if (buckets[index].hash == key.hashCode() && buckets[index].key != key) {
            return addEntryIntoTheEndOfTheChainAndIncreaseSize(buckets, key, value);
        }
        return addEntryIntoTheEndOfTheChainAndIncreaseSize(buckets, key, value);
    }

    @Override
    public V get(K key) {
        int index = getIndex(buckets, key);
        Entry<K,V> currentBucket = buckets[index];
        Entry<K,V> currentEntry;
        if (currentBucket == null) {
            return null;
        } else {
            currentEntry = currentBucket;
            if (currentEntry.next == null) {
                return currentEntry.value;
            }
            while (true) {
                if (currentEntry == null) {
                    return null;
                }
                if (currentEntry.key.equals(key)) {
                    return currentEntry.value;
                }
                currentEntry = currentEntry.next;
            }
        }
    }

    @Override
    public boolean containsKey(K key) {
        int index = getIndex(buckets, key);
        for (Entry<K, V> entry : buckets) {
            if (entry != null && entry.key.equals(key)) {
                return true;
            } else if (entry != null && entry.next != null) {
                while (true) {
                    if (buckets[index].key.equals(key)) {
                        return true;
                    }
                    buckets[index] = buckets[index].next;
                }
            }
        }
        return false;
    }

    @Override
    public V remove(K key) {
        V resultValue;
        int index = getIndex(buckets, key);
        if (buckets[index] == null) {
            throw new IllegalStateException("The bucket corresponding to the key " + key + " is empty");
        } else {
            if (buckets[index].key.equals(key)) {
                resultValue = buckets[index].value;
                buckets[index] = null;
                size--;
                return resultValue;
            }
        }
        while (true) {
            if (buckets[index].next.key.equals(key)) {
                resultValue = buckets[index].next.value;
                buckets[index].next = buckets[index].next.next;
                size--;
                return resultValue;
            }
            buckets[index] = buckets[index].next;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ", "{", "}");
        for (Entry<K, V> bucket : buckets) {
            if (bucket != null && bucket.next == null) {
                stringJoiner.add(bucket.toString());
            }
            if (bucket != null && bucket.next != null) {
                while (bucket.next != null) {
                    stringJoiner.add(bucket.toString());
                    bucket = bucket.next;
                }
                stringJoiner.add(bucket.toString());
            }
        }
        return stringJoiner.toString();
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] newBuckets = new Entry[INITIAL_CAPACITY * GROW_CONST];
        for (Map.Entry<K, V> entry : this) {
            int index = getIndex(newBuckets, entry.getKey());
            if (newBuckets[index] == null) {
                newBuckets[index] = (Entry<K, V>) entry;
                while (((Entry<K, V>) entry).next != null) {
                    newBuckets[index] = (Entry<K, V>) entry;
                    entry = ((Entry<K, V>) entry).next;
                }
            }
        }
        buckets = newBuckets;
    }

    private int getIndex(Entry<K, V>[] buckets, K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private Entry<K, V> getLastEntry(Entry<K, V> entry) {
        while(entry.next != null) {
            entry = entry.next;
        }
        return entry;
    }

    private V addEntryIntoTheEndOfTheChainAndIncreaseSize(Entry<K, V>[] buckets, K key, V value) {
        int index = getIndex(buckets, key);
        Entry<K, V> currentEntry = buckets[index];
        V resultValue = currentEntry.value;
        currentEntry = getLastEntry(currentEntry);
        currentEntry.next = new Entry<>(key, value);
        size++;
        return resultValue;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<Map.Entry<K, V>> {
        private int index;
        private int entryCount;
        boolean checkNext = false;
        Entry<K, V> currentEntry;
        Entry<K, V> currentBucket;

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
        private V value;
        private Entry<K, V> next;
        private int hash;

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
            if (key != null) {
                hash = key.hashCode();
            } else {
                throw new IllegalStateException("The KEY cannot be null");
            }
            return hash;
        }


    }



}
