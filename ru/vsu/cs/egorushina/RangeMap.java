package ru.vsu.cs.egorushina;

import java.util.Comparator;
import java.util.TreeSet;

public class RangeMap<K extends Comparable<K>, V> {

    // TreeSet для хранения записей, отсортированных по началу диапазона
    private TreeSet<RangeMapEntry<K, V>> entries = new TreeSet<>(
            Comparator.comparing(entry -> entry.range.lowerEndpoint())
    );

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

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            RangeMapEntry<?, ?> that = (RangeMapEntry<?, ?>) obj;
            return range.equals(that.range) && value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(range, value);
        }
    }

    public void put(Range<K> range, V value) {
        if (range.isEmpty()) {
            return;
        }

        TreeSet<RangeMapEntry<K, V>> newEntries = new TreeSet<>(
                Comparator.comparing(entry -> entry.range.lowerEndpoint())
        );
        TreeSet<RangeMapEntry<K, V>> toRemove = new TreeSet<>(
                Comparator.comparing(entry -> entry.range.lowerEndpoint())
        );

        // Находим все пересекающиеся записи
        for (RangeMapEntry<K, V> entry : entries) {
            if (entry.range.isConnected(range)) {
                toRemove.add(entry);

                // Сохраняем левую не пересекающуюся часть
                if (entry.range.lowerEndpoint().compareTo(range.lowerEndpoint()) < 0) {
                    Range<K> leftPart = Range.closedOpen(
                            entry.range.lowerEndpoint(),
                            range.lowerEndpoint()
                    );
                    if (!leftPart.isEmpty()) {
                        newEntries.add(new RangeMapEntry<>(leftPart, entry.value));
                    }
                }

                // Сохраняем правую не пересекающуюся часть
                if (entry.range.upperEndpoint().compareTo(range.upperEndpoint()) > 0) {
                    Range<K> rightPart = Range.closedOpen(
                            range.upperEndpoint(),
                            entry.range.upperEndpoint()
                    );
                    if (!rightPart.isEmpty()) {
                        newEntries.add(new RangeMapEntry<>(rightPart, entry.value));
                    }
                }
            } else {
                newEntries.add(entry);
            }
        }

        // Удаляем старые пересекающиеся записи и добавляем новые
        entries.removeAll(toRemove);
        entries.addAll(newEntries);

        // Добавляем новую запись
        entries.add(new RangeMapEntry<>(range, value));
    }

    public V get(K key) {
        // Быстрый поиск с использованием floor
        RangeMapEntry<K, V> searchKey = new RangeMapEntry<>(Range.closed(key, key), null);
        RangeMapEntry<K, V> candidate = entries.floor(searchKey);

        if (candidate != null && candidate.range.contains(key)) {
            return candidate.value;
        }
        return null;
    }

    public void remove(Range<K> range) {
        if (range.isEmpty()) {
            return;
        }

        TreeSet<RangeMapEntry<K, V>> toAdd = new TreeSet<>(
                Comparator.comparing(entry -> entry.range.lowerEndpoint())
        );
        TreeSet<RangeMapEntry<K, V>> toRemove = new TreeSet<>(
                Comparator.comparing(entry -> entry.range.lowerEndpoint())
        );

        for (RangeMapEntry<K, V> entry : entries) {
            if (entry.range.isConnected(range)) {
                toRemove.add(entry);

                // Сохраняем левую часть
                if (entry.range.lowerEndpoint().compareTo(range.lowerEndpoint()) < 0) {
                    Range<K> leftPart = Range.closedOpen(
                            entry.range.lowerEndpoint(),
                            range.lowerEndpoint()
                    );
                    if (!leftPart.isEmpty()) {
                        toAdd.add(new RangeMapEntry<>(leftPart, entry.value));
                    }
                }

                // Сохраняем правую часть
                if (entry.range.upperEndpoint().compareTo(range.upperEndpoint()) > 0) {
                    Range<K> rightPart = Range.closedOpen(
                            range.upperEndpoint(),
                            entry.range.upperEndpoint()
                    );
                    if (!rightPart.isEmpty()) {
                        toAdd.add(new RangeMapEntry<>(rightPart, entry.value));
                    }
                }
            }
        }

        entries.removeAll(toRemove);
        entries.addAll(toAdd);
    }

    public TreeSet<Range<K>> ranges() {
        TreeSet<Range<K>> rangeSet = new TreeSet<>(Comparator.comparing(Range::lowerEndpoint));
        for (RangeMapEntry<K, V> entry : entries) {
            rangeSet.add(entry.range);
        }
        return rangeSet;
    }

    public int size() {
        return entries.size();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public String toString() {
        return entries.toString();
    }
}