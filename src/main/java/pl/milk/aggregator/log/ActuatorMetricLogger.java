package pl.milk.aggregator.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.milk.aggregator.AggregatorApplication;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ActuatorMetricLogger {
    private static final long METRICS_INTERVAL = 300_000L;
    private static final Logger logger = LoggerFactory.getLogger("MetricsLogger");

    @Autowired
    private MetricsEndpoint metricsEndpoint;

    @Scheduled(fixedRate = METRICS_INTERVAL)
    public void logMetrics() {
        StringBuilder stringBuilder = new StringBuilder();
        metricsEndpoint.listNames()
                .getNames()
                .forEach(s -> parseMetric(stringBuilder, metricsEndpoint.metric(s, null)));
        logger.debug(stringBuilder.toString());
    }

    private String parseMetric(StringBuilder stringBuilder, MetricsEndpoint.MetricResponse metricResponse) {
        stringBuilder.append(metricResponse.getName()).append(": val[");
        metricResponse.getMeasurements().forEach(s -> stringBuilder.append(String.format("%.02f", s.getValue())));
        stringBuilder.append("]; ");
        return stringBuilder.toString();
    }


}
