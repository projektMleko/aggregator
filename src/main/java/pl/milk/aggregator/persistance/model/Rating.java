package pl.milk.aggregator.persistance.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_Sequence")
    @SequenceGenerator(name = "rating_Sequence", sequenceName = "RATING_SEQ")
    private Long id;
    private Long fkUserId;
    private Long fkMovieId;
    private BigDecimal value;
    private LocalDate timestamp;

    public Rating() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getFkUserId() {
        return this.fkUserId;
    }

    public void setFkUserId(final Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    public Long getFkMovieId() {
        return this.fkMovieId;
    }

    public void setFkMovieId(final Long fkMovieId) {
        this.fkMovieId = fkMovieId;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(final BigDecimal value) {
        this.value = value;
    }

    public LocalDate getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(final LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + this.id +
                ", fkUserId=" + this.fkUserId +
                ", fkMovieId=" + this.fkMovieId +
                ", value=" + this.value +
                ", timestamp=" + this.timestamp +
                '}';
    }
}
