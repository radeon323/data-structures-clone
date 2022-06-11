package com.luxoft.olshevchenko.map;

import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
public class HashMap<K, V> implements Map<K, V> {
    private static final int INITIAL_CAPACITY = 5;
    private static final int GROW_CONST = 2;
    private static final double LOAD_FACTOR = 0.75;

    private final Entry<K, V>[] buckets;
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
        return put(buckets, key, value);
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
        Entry<K, V> currentBucket = buckets[index];
        if (currentBucket == null) {
            throw new IllegalStateException("The bucket corresponding to the key " + key + " is empty");
        } else {
            if (currentBucket.key.equals(key)) {
                resultValue = currentBucket.value;
                buckets[index] = null;
                size--;
                return resultValue;
            }
            while (true) {
                if (currentBucket.next.key.equals(key)) {
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

    private V put(Entry<K, V>[] entries, K key, V value) {
        if (entries.length * LOAD_FACTOR <= size) {
            entries = resize(buckets);
        }
        int index = getIndex(entries, key);
        Entry<K,V> currentBucket = entries[index];
        if (currentBucket == null) {
            entries[index] = new Entry<>(key, value);
            size++;
            return null;
        } else {
            if (currentBucket.key.equals(key)) {
                V resultValue = currentBucket.value;
                entries[index].value = value;
                return resultValue;
            }
            while (currentBucket.next != null) {
                if (currentBucket.key.equals(key)) {
                    V resultValue = currentBucket.value;
                    entries[index].value = value;
                    return resultValue;
                } else if (currentBucket.hash == key.hashCode()) {
                    return addEntryIntoTheEndOfTheChainAndIncreaseSize(entries, key, value);
                }
                currentBucket = currentBucket.next;
            }
            return addEntryIntoTheEndOfTheChainAndIncreaseSize(entries, key, value);
        }
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V>[] resize(Entry<K, V>[] buckets) {
        Entry<K, V>[] newBuckets = new Entry[INITIAL_CAPACITY * GROW_CONST];
        for (Entry<K, V> bucket : buckets) {
            if (bucket != null) {
                int index = getIndex(newBuckets, bucket.getKey());
                if (bucket.next == null) {
                    newBuckets[index] = bucket;
                } else {
                    while (bucket.next != null) {
                        newBuckets[index] = getLastEntry(bucket);
                        bucket = bucket.next;
                    }
                }
            }
        }
        return newBuckets;
    }

    private int getIndex(Entry<K, V>[] buckets, K key) {
        if (key == null) {
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
        boolean checkNext = false;
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
                if (currentBucket == null) {
                    index++;
                } else {
                    if (currentBucket.next == null) {
                        index++;
                        entryCount++;
                        checkNext = true;
                    } else {
                        entryCount++;
                        checkNext = true;
                        currentBucket = currentBucket.next;
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
