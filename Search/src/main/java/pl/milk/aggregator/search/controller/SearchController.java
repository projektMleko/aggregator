package pl.milk.aggregator.search.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/search")
@Api(tags = {"Searching API"})
@SwaggerDefinition(tags = {
        @Tag(name = "Searching API", description = "API used to search movies, ratings, comments. Content can be cached to speedup response time.")})
public class SearchController {

    @GetMapping("/movie/{id}")
    String getMovieForId(String movieId) {
        return "movie";
    }

}
