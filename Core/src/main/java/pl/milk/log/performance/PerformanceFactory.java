package pl.milk.log.performance;

public class PerformanceFactory {


    public static PerformanceAction startMeasurePerformance(final String performanceName) {
        return new PerformanceAction(performanceName, System.nanoTime());
    }
}
