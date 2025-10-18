package ru.vsu.cs.egorushina;

import java.util.*;

public class RangeMap<K extends Comparable<K>, V> {

    private final TreeMap<Range<K>, V> entries = new TreeMap<>(
            (r1, r2) -> r1.lowerEndpoint().compareTo(r2.lowerEndpoint())
    );

    public void put(Range<K> range, V value) {
        if (range.isEmpty()) {
            return;
        }

        List<Range<K>> toRemove = new ArrayList<>();
        Map<Range<K>, V> toAdd = new HashMap<>();

        for (Range<K> existing : entries.keySet()) {
            if (existing.isConnected(range)) {
                toRemove.add(existing);

                // сохранение левой части
                if (existing.lowerEndpoint().compareTo(range.lowerEndpoint()) < 0) {
                    Range<K> leftPart = Range.closedOpen(
                            existing.lowerEndpoint(),
                            range.lowerEndpoint()
                    );
                    if (!leftPart.isEmpty()) {
                        toAdd.put(leftPart, entries.get(existing));
                    }
                }

                // правой части
                if (existing.upperEndpoint().compareTo(range.upperEndpoint()) > 0) {
                    Range<K> rightPart = Range.closedOpen(
                            range.upperEndpoint(),
                            existing.upperEndpoint()
                    );
                    if (!rightPart.isEmpty()) {
                        toAdd.put(rightPart, entries.get(existing));
                    }
                }
            }
        }

        for (Range<K> rangeToRemove : toRemove) {
            entries.remove(rangeToRemove);
        }

        entries.putAll(toAdd);  // ✅ ДОБАВИТЬ возврат сохранённых частей!
        entries.put(range, value);
    }

    public V get(K key) {
        for (Map.Entry<Range<K>, V> entry : entries.entrySet()) {
            if (entry.getKey().contains(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void remove(Range<K> range) {
        if (range.isEmpty()) {
            return;
        }

        List<Range<K>> toRemove = new ArrayList<>();
        Map<Range<K>, V> toAdd = new HashMap<>();

        for (Map.Entry<Range<K>, V> entry : entries.entrySet()) {
            Range<K> currentRange = entry.getKey();
            V currentValue = entry.getValue();

            if (currentRange.isConnected(range)) {
                toRemove.add(currentRange);

                // Левая часть
                if (currentRange.lowerEndpoint().compareTo(range.lowerEndpoint()) < 0) {
                    Range<K> leftPart = Range.closedOpen(
                            currentRange.lowerEndpoint(),
                            range.lowerEndpoint()
                    );
                    if (!leftPart.isEmpty()) {
                        toAdd.put(leftPart, currentValue);
                    }
                }

                // Правая часть
                if (currentRange.upperEndpoint().compareTo(range.upperEndpoint()) > 0) {
                    Range<K> rightPart = Range.closedOpen(
                            range.upperEndpoint(),
                            currentRange.upperEndpoint()
                    );
                    if (!rightPart.isEmpty()) {
                        toAdd.put(rightPart, currentValue);
                    }
                }
            }
        }

        for (Range<K> rangeToRemove : toRemove) {
            entries.remove(rangeToRemove);
        }
        entries.putAll(toAdd);
    }

    public Set<Range<K>> ranges() {
        return entries.keySet();
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