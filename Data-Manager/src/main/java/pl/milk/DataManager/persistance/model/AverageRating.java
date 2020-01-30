package pl.milk.DataManager.persistance.model;

import java.math.BigDecimal;

public class AverageRating {
    BigDecimal value;
    Long fkMovieId;

    public BigDecimal getValue() {
        return value;
    }

    public AverageRating setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    public Long getFkMovieId() {
        return fkMovieId;
    }

    public AverageRating setFkMovieId(Long fkMovieId) {
        this.fkMovieId = fkMovieId;
        return this;
    }
}
