package pl.milk.aggregator.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.milk.aggregator.AggregatorApplication;

public class HeapInfoLogger {
    private static final Logger logger = LoggerFactory.getLogger(AggregatorApplication.class);

    public static void logHeapInfo() {
        final long heapSize = Runtime.getRuntime().totalMemory();
        final long heapMaxSize = Runtime.getRuntime().maxMemory();
        final long heapFreeSize = Runtime.getRuntime().freeMemory();
        logger.info("heap-size: " + formatSize(heapSize));
        logger.info("heap-max-size: " + formatSize(heapMaxSize));
        logger.info("heap-free-size: " + formatSize(heapFreeSize));
    }

    private static String formatSize(final long v) {
        if (v < 1024) return v + " B";
        final int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double) v / (1L << (z * 10)), " KMGTPE".charAt(z));
    }
}
