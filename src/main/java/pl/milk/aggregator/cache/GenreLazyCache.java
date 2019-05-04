package pl.milk.aggregator.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.milk.aggregator.persistance.model.Genre;
import pl.milk.aggregator.persistance.repository.GenreRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GenreLazyCache {
    @Autowired
    private GenreRepository genreRepository;

    ConcurrentHashMap<String, Genre> genreMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void initGenreMap() {
        final List<Genre> allGenres = genreRepository.findAll();
        allGenres.forEach(g -> genreMap.put(g.getName(), g));
    }

    public Genre getGenreForName(final String name) {
        return genreMap.computeIfAbsent(name, this::addGenreForName);
    }

    private Genre addGenreForName(final String name) {
        final Genre genre = new Genre();
        genre.setName(name);
        return genreRepository.save(genre);
    }
}
