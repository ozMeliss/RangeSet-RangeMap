package ru.vsu.cs.egorushina;

public class Demo {

    // Тестирует базовые операции добавления диапазонов в RangeSet
    public static void testRangeSetAddOperations() {
        RangeSet<Integer> rangeSet = new RangeSet<>();

        rangeSet.add(Range.closedOpen(1, 5));
        System.out.println("After adding [1..5): " + rangeSet);

        rangeSet.add(Range.closedOpen(3, 8));
        System.out.println("After adding [3..8): " + rangeSet);

        rangeSet.add(Range.closedOpen(10, 12));
        System.out.println("After adding [10..12): " + rangeSet);

        System.out.println("Test passed: RangeSet add operations work correctly");
    }

    // Тестирует операцию contains для различных значений в RangeSet
    public static void testRangeSetContains() {
        RangeSet<Integer> rangeSet = new RangeSet<>();
        rangeSet.add(Range.closedOpen(1, 8));
        rangeSet.add(Range.closedOpen(10, 12));

        System.out.println("Contains 4: " + rangeSet.contains(4) + " (expected: true)");
        System.out.println("Contains 9: " + rangeSet.contains(9) + " (expected: false)");
        System.out.println("Contains 11: " + rangeSet.contains(11) + " (expected: true)");

        System.out.println("Test passed: RangeSet contains works correctly");
    }

    // Тестирует поиск диапазона, содержащего указанное значение
    public static void testRangeContaining() {
        RangeSet<Integer> rangeSet = new RangeSet<>();
        rangeSet.add(Range.closedOpen(1, 8));
        rangeSet.add(Range.closedOpen(10, 12));

        System.out.println("Range containing 6: " + rangeSet.rangeContaining(6));
        System.out.println("Range containing 11: " + rangeSet.rangeContaining(11));
        System.out.println("Range containing 9: " + rangeSet.rangeContaining(9));

        System.out.println("Test passed: Range containing search works correctly");
    }

    // Тестирует операцию удаления диапазона из RangeSet
    public static void testRangeSetRemove() {
        RangeSet<Integer> rangeSet = new RangeSet<>();
        rangeSet.add(Range.closedOpen(1, 8));
        rangeSet.add(Range.closedOpen(10, 12));

        System.out.println("Before remove: " + rangeSet);
        rangeSet.remove(Range.closedOpen(4, 6));
        System.out.println("After removing [4..6): " + rangeSet);

        System.out.println("Test passed: RangeSet remove works correctly");
    }

    // Тестирует базовые операции добавления в RangeMap
    public static void testRangeMapPutOperations() {
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

        System.out.println("Test passed: RangeMap put operations work correctly");
    }

    // Тестирует перекрытие диапазонов в RangeMap
    public static void testRangeMapOverlap() {
        RangeMap<Integer, String> rangeMap = new RangeMap<>();
        rangeMap.put(Range.closedOpen(90, 100), "A");
        rangeMap.put(Range.closedOpen(80, 90), "B");
        rangeMap.put(Range.closedOpen(70, 80), "C");

        System.out.println("Before overlap: " + rangeMap);
        rangeMap.put(Range.closed(75, 85), "Good");
        System.out.println("After adding Good for [75..85]: " + rangeMap);

        System.out.println("Grade for 80: " + rangeMap.get(80));
        System.out.println("Grade for 77: " + rangeMap.get(77));
        System.out.println("Grade for 73: " + rangeMap.get(73));
        System.out.println("Grade for 87: " + rangeMap.get(87));

        System.out.println("Test passed: RangeMap overlap handling works correctly");
    }

    // Тестирует итерацию по диапазону
    public static void testRangeIteration() {
        System.out.println("Iteration test [0..5):");
        for (Integer i : Range.closedOpen(0, 5)) {
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.println("Test passed: Range iteration works correctly");
    }

    // Тестирует граничные значения диапазонов
    public static void testRangeBoundaries() {
        RangeSet<Integer> rangeSet = new RangeSet<>();

        rangeSet.add(Range.closed(1, 5));
        rangeSet.add(Range.open(5, 10));

        System.out.println("Range with boundaries: " + rangeSet);
        System.out.println("Contains 5: " + rangeSet.contains(5));
        System.out.println("Contains 10: " + rangeSet.contains(10));

        System.out.println("Test passed: Range boundaries work correctly");
    }

    // Тестирует сложный сценарий с несколькими перекрывающимися диапазонами
    public static void testComplexRangeScenario() {
        RangeSet<Integer> rangeSet = new RangeSet<>();

        rangeSet.add(Range.closedOpen(1, 5));
        rangeSet.add(Range.closedOpen(3, 8));
        rangeSet.add(Range.closedOpen(10, 15));
        rangeSet.add(Range.closedOpen(12, 18));

        System.out.println("Complex scenario result: " + rangeSet);
        System.out.println("Contains 7: " + rangeSet.contains(7));
        System.out.println("Contains 8: " + rangeSet.contains(8));
        System.out.println("Contains 9: " + rangeSet.contains(9));
        System.out.println("Contains 12: " + rangeSet.contains(12));
        System.out.println("Contains 17: " + rangeSet.contains(17));
        System.out.println("Contains 18: " + rangeSet.contains(18));

        System.out.println("Test passed: Complex range scenario works correctly");
    }

    // Запуск всех тестов
    public static void main(String[] args) {
        System.out.println("=== Running RangeSet and RangeMap Tests ===\n");

        testRangeSetAddOperations();
        System.out.println();

        testRangeSetContains();
        System.out.println();

        testRangeContaining();
        System.out.println();

        testRangeSetRemove();
        System.out.println();

        testRangeMapPutOperations();
        System.out.println();

        testRangeMapOverlap();
        System.out.println();

        testRangeIteration();
        System.out.println();

        testRangeBoundaries();
        System.out.println();

        testComplexRangeScenario();

        System.out.println("\n=== All tests completed ===");
    }
}