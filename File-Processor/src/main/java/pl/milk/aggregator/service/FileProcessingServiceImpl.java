package pl.milk.aggregator.service;

import akka.actor.ActorSystem;
import akka.japi.pf.PFBuilder;
import akka.stream.ActorMaterializer;
import akka.stream.ActorMaterializerSettings;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Framing;
import akka.stream.javadsl.FramingTruncation;
import akka.stream.javadsl.Sink;
import akka.util.ByteString;
import org.springframework.beans.factory.annotation.Value;

import pl.milk.aggregator.processor.FileDataProcessor;
import pl.milk.aggregator.utlis.ByteStringDecoder;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.stream.BaseStream;


public class FileProcessingServiceImpl<T> implements FileProcessingService<T> {
    @Value("${file.processing.parallelism:4}")
    private int fileProcessingParallelism;
    @Value("${file.io.parallelism:8}")
    private int fileIOParallelism;


    private final FileDataProcessor<T> fileDataProcessor;
    private KafkaSenderService<T> kafkaSenderService;

    public FileProcessingServiceImpl(FileDataProcessor<T> fileDataProcessor, KafkaSenderService<T> kafkaSenderService) {
        this.fileDataProcessor = fileDataProcessor;
        this.kafkaSenderService = kafkaSenderService;
    }

    @Override
    public void processFile(String filePath) {
         fromPath(Paths.get(filePath))
                .skip(1)
                .parallel(4)
                .map(fileDataProcessor::processFileLine)
                .subscribe(t -> kafkaSenderService.sendModel(t));
    }

    private Flux<String> fromPath(Path path) {
        return Flux.using(() -> Files.lines(path),
                Flux::fromStream,
                BaseStream::close);
    }
}
