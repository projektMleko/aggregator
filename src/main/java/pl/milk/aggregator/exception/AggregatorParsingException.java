package pl.milk.aggregator.exception;

public class AggregatorParsingException extends RuntimeException {

    public AggregatorParsingException(final String message) {
        super(message);
    }

    public AggregatorParsingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
