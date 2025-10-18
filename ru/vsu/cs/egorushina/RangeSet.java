package ru.vsu.cs.egorushina;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class RangeSet<T extends Comparable<T>> implements Iterable<Range<T>> {

    // Используем TreeSet для автоматической сортировки и быстрого поиска
    private TreeSet<Range<T>> ranges = new TreeSet<>(
            Comparator.comparing(Range::lowerEndpoint)
    );

    public void add(Range<T> newRange) {
        if (newRange.isEmpty()) {
            return;
        }

        // Находим все диапазоны, которые пересекаются с новым
        TreeSet<Range<T>> toMerge = new TreeSet<>(Comparator.comparing(Range::lowerEndpoint));
        Range<T> mergedRange = newRange;

        // Итератор для безопасного удаления во время итерации
        Iterator<Range<T>> iterator = ranges.iterator();
        while (iterator.hasNext()) {
            Range<T> current = iterator.next();
            if (current.isConnected(mergedRange)) {
                toMerge.add(current);
                iterator.remove();
            }
        }

        // Объединяем все пересекающиеся диапазоны
        for (Range<T> range : toMerge) {
            mergedRange = mergedRange.span(range);
        }

        // Добавляем объединенный диапазон
        ranges.add(mergedRange);
    }

    public void remove(Range<T> rangeToRemove) {
        if (rangeToRemove.isEmpty()) {
            return;
        }

        TreeSet<Range<T>> toAdd = new TreeSet<>(Comparator.comparing(Range::lowerEndpoint));
        Iterator<Range<T>> iterator = ranges.iterator();

        while (iterator.hasNext()) {
            Range<T> current = iterator.next();

            if (current.isConnected(rangeToRemove)) {
                iterator.remove();

                // Добавляем левую часть, если она не пустая
                if (current.lowerEndpoint().compareTo(rangeToRemove.lowerEndpoint()) < 0) {
                    Range<T> leftPart = Range.closedOpen(
                            current.lowerEndpoint(),
                            rangeToRemove.lowerEndpoint()
                    );
                    if (!leftPart.isEmpty()) {
                        toAdd.add(leftPart);
                    }
                }

                // Добавляем правую часть, если она не пустая
                if (current.upperEndpoint().compareTo(rangeToRemove.upperEndpoint()) > 0) {
                    Range<T> rightPart = Range.closedOpen(
                            rangeToRemove.upperEndpoint(),
                            current.upperEndpoint()
                    );
                    if (!rightPart.isEmpty()) {
                        toAdd.add(rightPart);
                    }
                }
            }
        }

        // Добавляем все оставшиеся части
        ranges.addAll(toAdd);
    }

    public boolean contains(T value) {
        // Быстрый поиск с использованием ceiling/floor
        Range<T> floor = ranges.floor(Range.closed(value, value));
        return floor != null && floor.contains(value);
    }

    public Range<T> rangeContaining(T value) {
        // Находим диапазон, содержащий значение
        for (Range<T> range : ranges) {
            if (range.contains(value)) {
                return range;
            }
        }
        return null;

        // Альтернативная реализация с ceiling/floor для большей эффективности:
        // Range<T> searchRange = Range.closed(value, value);
        // Range<T> candidate = ranges.floor(searchRange);
        // if (candidate != null && candidate.contains(value)) {
        //     return candidate;
        // }
        // return null;
    }

    public RangeSet<T> complement() {
        // Базовая реализация - можно улучшить
        RangeSet<T> complement = new RangeSet<>();
        // TODO: Реализовать полноценное дополнение
        return complement;
    }

    public Range<T> span() {
        if (ranges.isEmpty()) {
            return null;
        }

        T lower = ranges.first().lowerEndpoint();
        T upper = ranges.last().upperEndpoint();

        return Range.closedOpen(lower, upper);
    }

    public boolean isEmpty() {
        return ranges.isEmpty();
    }

    public int size() {
        return ranges.size();
    }

    @Override
    public Iterator<Range<T>> iterator() {
        return ranges.iterator();
    }

    @Override
    public String toString() {
        return ranges.toString();
    }
}