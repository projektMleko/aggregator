package pl.milk.aggregator.controller;

import io.swagger.annotations.*;
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
@Api(tags = {"File processing"})
@SwaggerDefinition(tags = {
        @Tag(name = "File processing", description = "API used to import files into Database. Mostly used in development stage or before first production run.")})
public class FileProcessingController {

    @Autowired
    private FileProcessingService<Rating> ratingFileProcessingService;
    @Autowired
    private FileProcessingService<Movie> movieFileProcessingService;


    @ApiOperation(value = "Endpoint used to import rating file.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File path was successfully recognized and file was found."),
            @ApiResponse(code = 404, message = "File from given file path was not found.")})
    @PostMapping(path = "/ratings", consumes = "text/plain", produces = "application/json")
    public ResponseEntity<String> processRatings(@ApiParam(value = "Example value: C:/file/ratings.csv", required = true) @RequestBody final String filePath) {
        validateFilePath(filePath);
        ratingFileProcessingService.processFile(filePath);
        return new ResponseEntity<>("File exists, passed to processing service. Wish me luck.", HttpStatus.OK);
    }

    @ApiOperation(value = "Endpoint used to import movie file.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File path was successfully recognized and file was found."),
            @ApiResponse(code = 404, message = "File from given file path was not found.")})
    @PostMapping(path = "/movies", consumes = "text/plain", produces = "application/json")
    public ResponseEntity<String> processMovies(@ApiParam(value = "Example value: C:/file/movies.csv", required = true)  @RequestBody final String filePath) {
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
