package pl.milk.aggregator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.milk.aggregator.persistance.model.Movie;
import pl.milk.aggregator.persistance.model.Rating;
import pl.milk.aggregator.service.FileProcessingService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/file_processing")
public class FileProcessingController {

    @Autowired
    private FileProcessingService<Rating> ratingFileProcessingService;
    @Autowired
    private FileProcessingService<Movie> movieFileProcessingService;

    @RequestMapping(method = RequestMethod.OPTIONS, produces = "application/json")
    public ResponseEntity<List<String>> getPossibleEndpoints() {
        final List endpoints = new ArrayList();
        endpoints.add("OPTIONS: /");
        endpoints.add("POST: /ratings");
        endpoints.add("POST: /movies");
        return new ResponseEntity<>(endpoints, HttpStatus.OK);
    }

    @PostMapping(path = "/ratings", consumes = "text/plain", produces = "application/json")
    public ResponseEntity<String> processRatings(@RequestBody final String filePath) {
        validateFilePath(filePath);
        ratingFileProcessingService.processFile(filePath);
        return new ResponseEntity<>("File exists, passed to processing service. Wish me luck.", HttpStatus.OK);
    }

    @PostMapping(path = "/movies", consumes = "text/plain", produces = "application/json")
    public ResponseEntity<String> processMovies(@RequestBody final String filePath) {
        validateFilePath(filePath);
        movieFileProcessingService.processFile(filePath);
        return new ResponseEntity<>("File exists, passed to processing service. Wish me luck.", HttpStatus.OK);
    }

    private void validateFilePath(final String path) {
        final File f = new File(path);
        if (!f.exists() || f.isDirectory()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found!");
        }
    }
}
