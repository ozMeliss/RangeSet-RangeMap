package ru.vsu.cs.egorushina;

import java.util.Iterator;

public class RangeSet<T extends Comparable<T>> implements Iterable<Range<T>> {

    private SimpleLinkedList<Range<T>> ranges = new SimpleLinkedList<>();

    public void add(Range<T> range) {
        if (range.isEmpty()) {
            return;
        }

        SimpleLinkedList<Range<T>> result = new SimpleLinkedList<>();
        boolean added = false;

        try {
            Iterator<Range<T>> iterator = ranges.iterator();
            while (iterator.hasNext()) {
                Range<T> current = iterator.next();

                if (current.isConnected(range)) {
                    range = range.span(current);
                } else if (current.upperEndpoint().compareTo(range.lowerEndpoint()) < 0) {
                    result.addLast(current);
                } else {
                    if (!added) {
                        result.addLast(range);
                        added = true;
                    }
                    result.addLast(current);
                }
            }
        } catch (Exception e) {
        }

        if (!added) {
            result.addLast(range);
        }

        ranges = result;
    }

    public void remove(Range<T> range) {
        if (range.isEmpty()) {
            return;
        }

        SimpleLinkedList<Range<T>> result = new SimpleLinkedList<>();

        try {
            Iterator<Range<T>> iterator = ranges.iterator();
            while (iterator.hasNext()) {
                Range<T> current = iterator.next();

                if (!current.isConnected(range)) {
                    result.addLast(current);
                } else {
                    if (current.lowerEndpoint().compareTo(range.lowerEndpoint()) < 0) {
                        result.addLast(Range.closedOpen(current.lowerEndpoint(), range.lowerEndpoint()));
                    }
                    if (current.upperEndpoint().compareTo(range.upperEndpoint()) > 0) {
                        result.addLast(Range.closedOpen(range.upperEndpoint(), current.upperEndpoint()));
                    }
                }
            }
        } catch (Exception e) {
            // Ignore for iteration
        }

        ranges = result;
    }

    public boolean contains(T value) {
        try {
            Iterator<Range<T>> iterator = ranges.iterator();
            while (iterator.hasNext()) {
                Range<T> range = iterator.next();
                if (range.contains(value)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // Ignore for iteration
        }
        return false;
    }

    public Range<T> rangeContaining(T value) {
        try {
            Iterator<Range<T>> iterator = ranges.iterator();
            while (iterator.hasNext()) {
                Range<T> range = iterator.next();
                if (range.contains(value)) {
                    return range;
                }
            }
        } catch (Exception e) {
            // Ignore for iteration
        }
        return null;
    }

    public RangeSet<T> complement() {
        RangeSet<T> complement = new RangeSet<>();
        return complement;
    }

    public Range<T> span() {
        if (ranges.isEmpty()) {
            return null;
        }

        try {
            T lower = ranges.get(0).lowerEndpoint();
            T upper = ranges.get(0).upperEndpoint();

            for (int i = 1; i < ranges.size(); i++) {
                Range<T> current = ranges.get(i);
                if (current.lowerEndpoint().compareTo(lower) < 0) {
                    lower = current.lowerEndpoint();
                }
                if (current.upperEndpoint().compareTo(upper) > 0) {
                    upper = current.upperEndpoint();
                }
            }

            return Range.closedOpen(lower, upper);
        } catch (Exception e) {
            return null;
        }
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
        StringBuilder sb = new StringBuilder("[");
        try {
            Iterator<Range<T>> iterator = ranges.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next());
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
            }
        } catch (Exception e) {
            // Ignore for iteration
        }
        sb.append("]");
        return sb.toString();
    }
}