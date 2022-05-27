package com.luxoft.olshevchenko.map;

import com.luxoft.olshevchenko.list.ArrayList;
import com.luxoft.olshevchenko.list.List;

import java.util.*;

/**
 * @author Oleksandr Shevchenko
 */
public class HashMap<K, V> implements Map<K, V> {
    private static final int INITIAL_CAPACITY = 5;
    private static final int GROW_CONST = 2;

    private List<Entry<K, V>>[] buckets;
    private int size;

    public HashMap() {
        this(INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public HashMap(int length) {
        buckets = new ArrayList[length];
    }

    @Override
    public V put(K key, V value) {
        if (buckets.length == size) {
            resize();
        }
        List<Entry<K, V>> bucket = getBucket(key);
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                V resultValue = entry.value;
                entry.value = value;
                return resultValue;
            }
        }
        bucket.add(new Entry<>(key, value));
        size++;
        return null;
    }

    @Override
    public V get(K key) {
        V resultValue = null;
        List<Entry<K, V>> bucket = getBucket(key);
        if (bucket.isEmpty()) {
            return null;
        } else {
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    resultValue = entry.value;
                }
            }
        }
        return resultValue;
    }

    @Override
    public boolean containsKey(K key) {
        for (Entry<K, V> entry : getBucket(key)) {
            if (entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V remove(K key) {
        int count = 0;
        V resultValue = null;
        List<Entry<K, V>> bucket = getBucket(key);
        if (bucket.isEmpty()) {
            throw new IllegalStateException("The bucket corresponding to the key " + key + " is empty");
        } else {
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    resultValue = entry.value;
                    bucket.remove(count);
                    size--;
                }
                count++;
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
        for (List<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                stringJoiner.add(bucket.toString());
            }
        }
        return stringJoiner.toString();
    }

    private void resize() {
        List<Entry<K, V>>[] newBuckets = new ArrayList[INITIAL_CAPACITY * GROW_CONST];
        for (Map.Entry<K, V> entry : this) {
            int index = getIndex(newBuckets, entry.getKey());
            if (newBuckets[index] == null) {
                newBuckets[index] = new ArrayList<>();
            }
            newBuckets[index].add((Entry<K, V>) entry);
        }
        buckets = newBuckets;
    }

    private List<Entry<K, V>> getBucket(K key) {
        return getBucket(buckets, key);
    }

    private List<Entry<K, V>> getBucket(List<Entry<K, V>>[] buckets, K key) {
        int index = getIndex(buckets, key);
        if (buckets[index] == null) {
            buckets[index] = new ArrayList<>();
        }
        return buckets[index];
    }

    private int getIndex(List<Entry<K, V>>[] buckets, K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<Map.Entry<K, V>> {
        private int index;
        boolean checkNext = false;
        private Iterator<Entry<K, V>> iterator;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Entry<K, V> next() {
            if (hasNext()) {
                List<Entry<K, V>> bucket = buckets[index];
                iterator = bucket.iterator();
                Entry<K, V> next = iterator.next();
                index++;
                checkNext = true;
                return next;
            } else {
                throw new NoSuchElementException("There is no next element in the map");
            }

        }

        @Override
        public void remove() {
            if (checkNext) {
                iterator.remove();
                size--;
                checkNext = false;
            } else {
                throw new IllegalStateException("Called remove method without next");
            }
        }

    }


    private static class Entry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
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
    }


}
