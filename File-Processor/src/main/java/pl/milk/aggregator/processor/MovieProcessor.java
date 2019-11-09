package pl.milk.aggregator.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.milk.aggregator.cache.GenreLazyCache;
import pl.milk.aggregator.persistance.model.Genre;
import pl.milk.aggregator.persistance.model.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MovieProcessor implements FileDataProcessor<Movie> {
    private static final String VALUE_SEPARATOR = ",";
    private static final String GENRE_SEPARATOR = "\\|";

    @Autowired
    private GenreLazyCache genreLazyCache;

    @Override
    public Movie processFileLine(final String fileLine) {
        final String[] values = split(fileLine);
        final Movie movie = new Movie();
        movie.setId(Long.valueOf(values[0]));
        movie.setTitle(values[1]);
        splitToGenres(values[2]).forEach(movie::addGenre);
        return movie;
    }

    private String[] split(final String fileLine) {
        final String[] values = new String[3];
        final int comaFirstPosition = fileLine.indexOf(VALUE_SEPARATOR);
        final int comaLastPosition = fileLine.lastIndexOf(VALUE_SEPARATOR);
        values[0] = fileLine.substring(0, comaFirstPosition).replace(VALUE_SEPARATOR, "");
        values[1] = fileLine.substring(comaFirstPosition, comaLastPosition).replace(VALUE_SEPARATOR, "");
        values[2] = fileLine.substring(comaLastPosition).replace(VALUE_SEPARATOR, "");
        return values;
    }

    private List<Genre> splitToGenres(final String genreLine) {
        final List<String> values = Arrays.asList(genreLine.split(GENRE_SEPARATOR));
        final List<Genre> genreList = new ArrayList<>();
        values.forEach(g -> genreList.add(genreLazyCache.getGenreForName(g)));
        return genreList;
    }
}
