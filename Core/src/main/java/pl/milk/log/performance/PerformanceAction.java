package pl.milk.log.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceAction {

    private static final Logger logger = LoggerFactory.getLogger("PerfLogger");

    final String actionName;
    final long startTime;
    final double nanoToMiliDivider = 1_000_000;

    public PerformanceAction(final String actionName, final long startTime) {
        this.actionName = actionName;
        this.startTime = startTime;
    }

    public void finishAction() {
        final double duration = (System.nanoTime() - startTime) / nanoToMiliDivider;
        logger.info("Action finished: {}, time: {}ms", actionName, duration);
    }
}
