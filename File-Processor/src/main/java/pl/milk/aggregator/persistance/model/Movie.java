package pl.milk.aggregator.persistance.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Movie {
    @Id
    private Long id;
    @Column(unique = true)
    private String title;
    @ManyToMany(cascade = {
            CascadeType.MERGE
    })
    @JoinTable(
            name = "movie_to_genre",
            joinColumns = @JoinColumn(name = "MOVIE_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID"))
    private final List<Genre> genres = new ArrayList<>();

    public Movie() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void addGenre(final Genre genre) {
        //  genre.getMovies().add(this);
        genres.add(genre);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genres=" + genres +
                '}';
    }
}
