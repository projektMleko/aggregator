package pl.milk.aggregator.processor;

import org.springframework.stereotype.Component;
import pl.milk.aggregator.exception.AggregatorParsingException;
import pl.milk.aggregator.persistance.model.Rating;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

@Component
public class RatingProcessor implements FileDataProcessor<Rating> {

    private static final String VALUE_SEPARATOR = ",";

    @Override
    public Rating processFileLine(final String fileLine) {
        final String[] values = fileLine.split(VALUE_SEPARATOR);
        final Rating rating = new Rating();
        rating.setFkMovieId(Long.valueOf(values[0]));
        rating.setFkUserId(Long.valueOf(values[1]));
        rating.setValue(new BigDecimal(values[2]));
        rating.setTimestamp(processStringToTimeStamp(values[3]));
        return rating;
    }

    private LocalDate processStringToTimeStamp(final String stringTimestamp) {
        final LocalDate timestamp;
        try {
            final Long longTimestamp = Long.parseLong(stringTimestamp);
            timestamp = Instant.ofEpochSecond(longTimestamp).atZone(ZoneOffset.UTC).toLocalDate();
        } catch (final DateTimeParseException ex) {
            throw new AggregatorParsingException(String.format("Error during parsing timestamp string: %s into LocalDateTime object.", stringTimestamp), ex);
        }
        return timestamp;
    }
}
