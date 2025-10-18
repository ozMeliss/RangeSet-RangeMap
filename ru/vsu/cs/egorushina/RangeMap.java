package ru.vsu.cs.egorushina;

import java.util.*;

public class RangeMap<K extends Comparable<K>, V> {

    // Используем TreeMap для хранения записей, ключ - нижняя граница диапазона
    private final TreeMap<Range<K>, V> entries = new TreeMap<>(
            Comparator.comparing(Range::lowerEndpoint)
    );

    public void put(Range<K> range, V value) {
        if (range.isEmpty()) {
            return;
        }

        // Находим все пересекающиеся диапазоны
        List<Range<K>> toRemove = new ArrayList<>();
        Map<Range<K>, V> toAdd = new HashMap<>();

        // Итерация по всем записям для поиска пересечений
        for (Map.Entry<Range<K>, V> entry : entries.entrySet()) {
            Range<K> currentRange = entry.getKey();
            V currentValue = entry.getValue();

            if (currentRange.isConnected(range)) {
                toRemove.add(currentRange);

                // Сохраняем левую не пересекающуюся часть
                if (currentRange.lowerEndpoint().compareTo(range.lowerEndpoint()) < 0) {
                    Range<K> leftPart = Range.closedOpen(
                            currentRange.lowerEndpoint(),
                            range.lowerEndpoint()
                    );
                    if (!leftPart.isEmpty()) {
                        toAdd.put(leftPart, currentValue);
                    }
                }

                // Сохраняем правую не пересекающуюся часть
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

        // Удаляем старые пересекающиеся записи
        for (Range<K> rangeToRemove : toRemove) {
            entries.remove(rangeToRemove);
        }

        // Добавляем новые части
        entries.putAll(toAdd);

        // Добавляем новую запись
        entries.put(range, value);
    }

    public V get(K key) {
        // Эффективный поиск через floorEntry/ceilingEntry
        // Создаем временный диапазон для поиска
        Range<K> searchRange = Range.closed(key, key);

        // Ищем ближайший диапазон, который может содержать ключ
        Map.Entry<Range<K>, V> floorEntry = entries.floorEntry(searchRange);
        if (floorEntry != null && floorEntry.getKey().contains(key)) {
            return floorEntry.getValue();
        }

        Map.Entry<Range<K>, V> ceilingEntry = entries.ceilingEntry(searchRange);
        if (ceilingEntry != null && ceilingEntry.getKey().contains(key)) {
            return ceilingEntry.getValue();
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

                // Сохраняем левую часть
                if (currentRange.lowerEndpoint().compareTo(range.lowerEndpoint()) < 0) {
                    Range<K> leftPart = Range.closedOpen(
                            currentRange.lowerEndpoint(),
                            range.lowerEndpoint()
                    );
                    if (!leftPart.isEmpty()) {
                        toAdd.put(leftPart, currentValue);
                    }
                }

                // Сохраняем правую часть
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

        // Удаляем и добавляем
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