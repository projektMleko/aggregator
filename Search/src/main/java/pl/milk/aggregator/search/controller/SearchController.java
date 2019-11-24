package pl.milk.aggregator.search.controller;

import com.hazelcast.core.HazelcastInstance;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/search")
@Api(tags = {"Searching API"})
@SwaggerDefinition(tags = {
        @Tag(name = "Searching API", description = "API used to search movies, ratings, comments. Content can be cached to speedup response time.")})
public class SearchController {

    @Autowired
    HazelcastInstance hazelcastInstance;

    @GetMapping("/movie/{id}")
    String getMovieForId(@PathVariable("id") String movieId) {
        List movieCacheList = hazelcastInstance.getList("MOVIE-CACHE");
        movieCacheList.add("movie-" + movieId);
        return movieCacheList.stream().collect(Collectors.joining(",")).toString();
    }

}
