package ru.vsu.cs.egorushina;

import java.util.*;

public class RangeSet<T extends Comparable<T>> implements Iterable<Range<T>> {
    //начало диапазон
    private final TreeMap<T, Range<T>> ranges = new TreeMap<>();

    public void add(Range<T> newRange) {
        if (newRange.isEmpty()) {
            return;
        }
        Iterator<Range<T>> iterator = ranges.values().iterator();
        while (iterator.hasNext()) {
            Range<T> existing = iterator.next();
            if (existing.isConnected(newRange)) {
                iterator.remove();
                newRange = existing.span(newRange);
            }
        }

        ranges.put(newRange.lowerEndpoint(), newRange);
    }

    public void remove(Range<T> rangeToRemove) {
        if (rangeToRemove.isEmpty()) {
            return;
        }

        List<Range<T>> toAdd = new ArrayList<>();
        Iterator<Range<T>> iterator = ranges.values().iterator();

        while (iterator.hasNext()) {
            Range<T> current = iterator.next();

            if (current.isConnected(rangeToRemove)) {
                iterator.remove();

                // Левая часть
                if (current.lowerEndpoint().compareTo(rangeToRemove.lowerEndpoint()) < 0) {
                    toAdd.add(Range.closedOpen(current.lowerEndpoint(), rangeToRemove.lowerEndpoint()));
                }

                // Правая часть
                if (current.upperEndpoint().compareTo(rangeToRemove.upperEndpoint()) > 0) {
                    toAdd.add(Range.closedOpen(rangeToRemove.upperEndpoint(), current.upperEndpoint()));
                }
            }
        }

        // Добавляем оставшиеся части
        for (Range<T> range : toAdd) {
            add(range);
        }
    }

    public boolean contains(T value) {
        Range<T> range = rangeContaining(value);
        return range != null;
    }

    public Range<T> rangeContaining(T value) {

        Map.Entry<T, Range<T>> floorEntry = ranges.floorEntry(value);
        if (floorEntry != null && floorEntry.getValue().contains(value)) {
            return floorEntry.getValue();
        }
        return null;
    }

    public RangeSet<T> complement() {
        RangeSet<T> result = new RangeSet<>();
        return result;
    }

    public Range<T> span() {
        if (ranges.isEmpty()) {
            return null;
        }
        return Range.closedOpen(
                ranges.firstEntry().getValue().lowerEndpoint(),
                ranges.lastEntry().getValue().upperEndpoint()
        );
    }

    public boolean isEmpty() {
        return ranges.isEmpty();
    }

    public int size() {
        return ranges.size();
    }

    @Override
    public Iterator<Range<T>> iterator() {
        return ranges.values().iterator();
    }

    @Override
    public String toString() {
        return ranges.values().toString();
    }
}