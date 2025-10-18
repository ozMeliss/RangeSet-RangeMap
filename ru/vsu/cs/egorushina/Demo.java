package ru.vsu.cs.egorushina;

public class Demo {
    public static void main(String[] args) {
        System.out.println("=== RangeSet Demo ===");

        RangeSet<Integer> rangeSet = new RangeSet<>();

        rangeSet.add(Range.closedOpen(1, 5));
        System.out.println("After adding [1..5): " + rangeSet);

        rangeSet.add(Range.closedOpen(3, 8));
        System.out.println("After adding [3..8): " + rangeSet);

        rangeSet.add(Range.closedOpen(10, 12));
        System.out.println("After adding [10..12): " + rangeSet);

        System.out.println("Contains 4: " + rangeSet.contains(4));
        System.out.println("Contains 9: " + rangeSet.contains(9));
        System.out.println("Range containing 6: " + rangeSet.rangeContaining(6));

        rangeSet.remove(Range.closedOpen(4, 6));
        System.out.println("After removing [4..6): " + rangeSet);

        System.out.println("\n=== RangeMap Demo ===");

        RangeMap<Integer, String> rangeMap = new RangeMap<>();

        rangeMap.put(Range.closedOpen(90, 100), "A");
        rangeMap.put(Range.closedOpen(80, 90), "B");
        rangeMap.put(Range.closedOpen(70, 80), "C");
        rangeMap.put(Range.closed(0, 70), "F");

        System.out.println("Grade ranges: " + rangeMap);

        System.out.println("Grade for 95: " + rangeMap.get(95));
        System.out.println("Grade for 85: " + rangeMap.get(85));
        System.out.println("Grade for 75: " + rangeMap.get(75));
        System.out.println("Grade for 65: " + rangeMap.get(65));

        rangeMap.put(Range.closed(75, 85), "Good");
        System.out.println("After adding Good for [75..85]: " + rangeMap);

        System.out.println("Grade for 80: " + rangeMap.get(80));
        System.out.println("Grade for 77: " + rangeMap.get(77));

        // Test iteration
        System.out.println("\n=== Iteration Test ===");
        for (Integer i : Range.closedOpen(0, 5)) {
            System.out.print(i + " ");
        }
    }
}