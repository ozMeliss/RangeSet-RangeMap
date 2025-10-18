package ru.vsu.cs.egorushina;

import java.util.Iterator;

public class RangeMap<K extends Comparable<K>, V> {

    private SimpleLinkedList<RangeMapEntry<K, V>> entries = new SimpleLinkedList<>();

    private static class RangeMapEntry<K extends Comparable<K>, V> {
        public Range<K> range;
        public V value;

        public RangeMapEntry(Range<K> range, V value) {
            this.range = range;
            this.value = value;
        }

        @Override
        public String toString() {
            return range + "=" + value;
        }
    }

    public void put(Range<K> range, V value) {
        if (range.isEmpty()) {
            return;
        }

        // Remove overlapping ranges
        SimpleLinkedList<RangeMapEntry<K, V>> result = new SimpleLinkedList<>();

        try {
            Iterator<RangeMapEntry<K, V>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                RangeMapEntry<K, V> entry = iterator.next();

                if (!entry.range.isConnected(range)) {
                    result.addLast(entry);
                } else {
                    // Add non-overlapping parts of existing entry
                    if (entry.range.lowerEndpoint().compareTo(range.lowerEndpoint()) < 0) {
                        result.addLast(new RangeMapEntry<>(
                                Range.closedOpen(entry.range.lowerEndpoint(), range.lowerEndpoint()),
                                entry.value
                        ));
                    }
                    if (entry.range.upperEndpoint().compareTo(range.upperEndpoint()) > 0) {
                        result.addLast(new RangeMapEntry<>(
                                Range.closedOpen(range.upperEndpoint(), entry.range.upperEndpoint()),
                                entry.value
                        ));
                    }
                }
            }
        } catch (Exception e) {
            // Ignore for iteration
        }

        // Add new entry
        result.addLast(new RangeMapEntry<>(range, value));
        entries = result;
    }

    public V get(K key) {
        try {
            Iterator<RangeMapEntry<K, V>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                RangeMapEntry<K, V> entry = iterator.next();
                if (entry.range.contains(key)) {
                    return entry.value;
                }
            }
        } catch (Exception e) {
            // Ignore for iteration
        }
        return null;
    }

    public void remove(Range<K> range) {
        if (range.isEmpty()) {
            return;
        }

        SimpleLinkedList<RangeMapEntry<K, V>> result = new SimpleLinkedList<>();

        try {
            Iterator<RangeMapEntry<K, V>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                RangeMapEntry<K, V> entry = iterator.next();

                if (!entry.range.isConnected(range)) {
                    result.addLast(entry);
                } else {
                    // Keep only non-overlapping parts
                    if (entry.range.lowerEndpoint().compareTo(range.lowerEndpoint()) < 0) {
                        result.addLast(new RangeMapEntry<>(
                                Range.closedOpen(entry.range.lowerEndpoint(), range.lowerEndpoint()),
                                entry.value
                        ));
                    }
                    if (entry.range.upperEndpoint().compareTo(range.upperEndpoint()) > 0) {
                        result.addLast(new RangeMapEntry<>(
                                Range.closedOpen(range.upperEndpoint(), entry.range.upperEndpoint()),
                                entry.value
                        ));
                    }
                }
            }
        } catch (Exception e) {
            // Ignore for iteration
        }

        entries = result;
    }

    public SimpleLinkedList<Range<K>> ranges() {
        SimpleLinkedList<Range<K>> rangeList = new SimpleLinkedList<>();
        try {
            Iterator<RangeMapEntry<K, V>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                rangeList.addLast(iterator.next().range);
            }
        } catch (Exception e) {
            // Ignore for iteration
        }
        return rangeList;
    }

    public int size() {
        return entries.size();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        try {
            Iterator<RangeMapEntry<K, V>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next());
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
            }
        } catch (Exception e) {
            // Ignore for iteration
        }
        sb.append("}");
        return sb.toString();
    }
}