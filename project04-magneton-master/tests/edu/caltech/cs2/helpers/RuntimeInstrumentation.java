package edu.caltech.cs2.helpers;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RuntimeInstrumentation {
    private static final int SKIP = 5;
    private static final int ITERATIONS = 100;

    public enum ComplexityType {
        CONSTANT(0, "constant"),
        LOGARITHMIC(1, "logarithmic"),
        LINEAR(2, "linear"),
        QUADRATIC(3, "quadratic"),
        WORSE(4, "worse than quadratic");

        private final String name;
        private int size;

        ComplexityType(int size, String name) {
            this.size = size;
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public boolean isSlowerThan(ComplexityType other) {
            return this.size > other.size;
        }
    }

    public static <DS> long timeFunction(DS ds, Consumer<DS> function) {
        long startTime = System.nanoTime();
        function.accept(ds);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public static <DS> ComplexityType getEmpiricalComplexity(Function<Integer, DS> provideDSOfSize, Consumer<DS> functionToTest, int numberOfDoubles) {
        List<Long> times = new ArrayList<>();
        int maxSize = (1 << (numberOfDoubles + SKIP));
        for (int currentSize = 1; currentSize < maxSize; currentSize *= 2) {
            long totalTime = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                DS ds = provideDSOfSize.apply(currentSize);
                totalTime += timeFunction(ds, functionToTest);
            }
            times.add(Math.round((double)totalTime / ITERATIONS));
        }

        for (int i = 0; i < SKIP; i++) {
            times.remove(0);
        }

        if (isApproximately(ComplexityType.CONSTANT, times)) {
            return ComplexityType.CONSTANT;
        }
        else if (isApproximately(ComplexityType.LOGARITHMIC, times)) {
            return ComplexityType.LOGARITHMIC;
        }
        else if (isApproximately(ComplexityType.LINEAR, times)) {
            return ComplexityType.LINEAR;
        }
        else if (isApproximately(ComplexityType.QUADRATIC, times)) {
            return ComplexityType.QUADRATIC;
        }
        else {
            return ComplexityType.WORSE;
        }
    }

    private static boolean isApproximately(ComplexityType type, List<Long> times) {
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            int numElements = (1 << (i + SKIP));
            double d = 0.0;
            switch (type) {
                case CONSTANT:
                    d = times.get(i);
                    break;
                case LOGARITHMIC:
                    d = times.get(i) / (Math.log10(numElements) / Math.log10(2));
                    break;
                case LINEAR:
                    d = ((double)times.get(i)) / numElements;
                    break;
                case QUADRATIC:
                    d = ((double)times.get(i)) / (numElements * numElements);
                    break;
                default:
                    throw new RuntimeException("unimplemented isApproximately for " + type);
            }
            values.add(d);
        }

        List<Double> differences = new ArrayList<>();
        for (int i = 1; i < values.size(); i++) {
            differences.add(values.get(i) - values.get(0));
        }
        Collections.sort(differences);

        double medianDifference = differences.get(differences.size() / 2);

        System.out.println(type + ", " + values + ", " + medianDifference);

        return medianDifference < 0;
    }

    public static <DS> void assertAtMost(String whatIsBeingTested, ComplexityType expected, Function<Integer, DS> provideDSOfSize, Consumer<DS> functionToTest, int numberOfDoubles) {
        ComplexityType calculated = getEmpiricalComplexity(provideDSOfSize, functionToTest, numberOfDoubles);
        if (calculated.isSlowerThan(expected)) {
            fail(whatIsBeingTested + " is expected to be " + expected + " time or better. The actual calculated time is " + calculated + ".\nThis test is non-deterministic which means it might not always be correct.  If you run it multiple times and it usually passes, that's probably fine.");
        }
    }
}
