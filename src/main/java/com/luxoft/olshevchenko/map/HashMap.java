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

    public HashMap() {
        buckets = new Entry[INITIAL_CAPACITY];
    }


    @Override
    public V put(K key, V value) {
        V resultValue;
        int index = getIndex(buckets, key);
        Entry<K, V> bucket = buckets[index];
        if (buckets.length == size) {
            resize();
        }
        if (buckets[index] == null) {
            buckets[index] = new Entry<>(key, value);
            size++;
            return null;
        }
        if (bucket.hash == key.hashCode()) {
            V oldValue = bucket.value;
            Entry<K, V> currentBucket = getLastEntry(bucket);
            Entry<K, V> newBucket = new Entry<>(key, value);
            newBucket.value = oldValue;
            currentBucket.next = newBucket;
            bucket.value = value;
            resultValue = newBucket.value;
            return resultValue;
        }
        return null;
    }

    @Override
    public V get(K key) {
        Entry<K, V> bucket = getBucket(key);
        if (bucket == null) {
            return null;
        } else {
            return bucket.value;
        }
    }

    @Override
    public boolean containsKey(K key) {
        for (Entry<K, V> bucket : buckets) {
            if (bucket != null && bucket.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V remove(K key) {
        V resultValue = null;

        Entry<K, V> bucket = getBucket(key);
        int index = getIndex(buckets, key);

        if (getBucket(key).value == null) {
            throw new IllegalStateException("The bucket corresponding to the key " + key + " is empty");
        } else {
            if (bucket.key.equals(key)) {
                resultValue = bucket.value;
                buckets[index] = null;
                size--;
            }
        }
        return resultValue;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ", "{", "}");
        for (Entry<K, V> bucket : buckets) {
            if (bucket != null) {
                stringJoiner.add(bucket.toString());
            }
        }
        return stringJoiner.toString();
    }

    private void resize() {
        Entry<K, V>[] newBuckets = new Entry[INITIAL_CAPACITY * GROW_CONST];
        for (Map.Entry<K, V> entry : this) {
            int index = getIndex(newBuckets, entry.getKey());
            if (newBuckets[index] == null) {
                newBuckets[index] = (Entry<K, V>) entry;
            } else {
                newBuckets[index] = getLastEntry((Entry<K, V>) entry);
            }
        }
        buckets = newBuckets;
    }

    private Entry<K, V> getBucket(K key) {
        return getBucket(buckets, key);
    }

    private Entry<K, V> getBucket(Entry<K, V>[] buckets, K key) {
        int index = getIndex(buckets, key);
        if (buckets[index] == null) {
            buckets[index] = new Entry<>(key, null);
        }
        return buckets[index];
    }

    private int getIndex(Entry<K, V>[] buckets, K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private Entry<K, V> getLastEntry(Entry<K, V> bucket) {
        while(bucket.next != null) {
            bucket = bucket.next;
        }
        return bucket;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<Map.Entry<K, V>> {
        private int index;
        boolean checkNext = false;
        Entry<K, V> currentBucket;

        @Override
        public boolean hasNext() {
            return index != size;
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There is no next element in the map");
            }
            currentBucket = buckets[index];
            if (currentBucket.next != null) {
                checkNext = true;
                return currentBucket.next;
            }
            index++;
            checkNext = true;
            return currentBucket;
        }

        @Override
        public void remove() {
            if (!checkNext) {
                throw new IllegalStateException("Called remove method without next");
            }
            HashMap.this.remove(currentBucket.key);
            size--;
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
