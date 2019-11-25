package pl.milk.aggregator.exception;

public class AggregatorPersistanceException extends RuntimeException {

    public AggregatorPersistanceException(final String message) {
        super(message);
    }

    public AggregatorPersistanceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
