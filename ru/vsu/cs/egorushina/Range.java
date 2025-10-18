package ru.vsu.cs.egorushina;

import java.util.Iterator;

public class Range<T extends Comparable<T>> implements Iterable<T> {
    private T first;
    private T last;
    private boolean closedFirst;
    private boolean closedLast;

    private Range(T first, T last, boolean closedFirst, boolean closedLast) {
        this.first = first;
        this.last = last;
        this.closedFirst = closedFirst;
        this.closedLast = closedLast;
    }

    public static <T extends Comparable<T>> Range<T> closedOpen(T first, T last) {
        return new Range<>(first, last, true, false);
    }

    public static <T extends Comparable<T>> Range<T> closed(T first, T last) {
        return new Range<>(first, last, true, true);
    }

    public static <T extends Comparable<T>> Range<T> open(T first, T last) {
        return new Range<>(first, last, false, false);
    }

    public static <T extends Comparable<T>> Range<T> openClosed(T first, T last) {
        return new Range<>(first, last, false, true);
    }

    public boolean contains(T value) {
        boolean lowerOk = closedFirst ? value.compareTo(first) >= 0 : value.compareTo(first) > 0;
        boolean upperOk = closedLast ? value.compareTo(last) <= 0 : value.compareTo(last) < 0;
        return lowerOk && upperOk;
    }

    public boolean isConnected(Range<T> other) {
        return this.contains(other.first) || this.contains(other.last) ||
                other.contains(this.first) || other.contains(this.last) ||
                this.first.equals(other.last) || this.last.equals(other.first);
    }

    public Range<T> span(Range<T> other) {
        T lower = this.first.compareTo(other.first) < 0 ? this.first : other.first;
        T upper = this.last.compareTo(other.last) > 0 ? this.last : other.last;
        boolean lowerClosed = this.first.compareTo(other.first) < 0 ? this.closedFirst : other.closedFirst;
        boolean upperClosed = this.last.compareTo(other.last) > 0 ? this.closedLast : other.closedLast;

        return new Range<>(lower, upper, lowerClosed, upperClosed);
    }

    public boolean isEmpty() {
        if (first.compareTo(last) > 0) return true;
        if (first.equals(last) && (!closedFirst || !closedLast)) return true;
        return false;
    }

    public T lowerEndpoint() {
        return first;
    }

    public T upperEndpoint() {
        return last;
    }

    @Override
    public Iterator<T> iterator() {
        // Simple implementation for Integer ranges
        if (first instanceof Integer && last instanceof Integer) {
            return (Iterator<T>) new Iterator<Integer>() {
                int curr = (Integer) first;
                final int end = (Integer) last;

                @Override
                public boolean hasNext() {
                    return closedLast ? curr <= end : curr < end;
                }

                @Override
                public Integer next() {
                    if (!hasNext()) throw new java.util.NoSuchElementException();
                    return closedFirst ? curr++ : (curr++ + 1);
                }
            };
        }
        throw new UnsupportedOperationException("Iteration supported only for Integer ranges");
    }

    @Override
    public String toString() {
        String left = closedFirst ? "[" : "(";
        String right = closedLast ? "]" : ")";
        return left + first + ".." + last + right;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Range<?> range = (Range<?>) obj;
        return first.equals(range.first) && last.equals(range.last) &&
                closedFirst == range.closedFirst && closedLast == range.closedLast;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(first, last, closedFirst, closedLast);
    }
}