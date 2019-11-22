package pl.milk.aggregator.service;

import akka.actor.ActorSystem;
import akka.japi.pf.PFBuilder;
import akka.stream.ActorMaterializer;
import akka.stream.ActorMaterializerSettings;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Framing;
import akka.stream.javadsl.FramingTruncation;
import akka.util.ByteString;
import org.springframework.beans.factory.annotation.Value;
import pl.milk.aggregator.log.performance.PerformanceAction;
import pl.milk.aggregator.log.performance.PerformanceFactory;
import pl.milk.aggregator.processor.FileDataProcessor;
import pl.milk.aggregator.utlis.ByteStringDecoder;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class FileProcessingServiceImpl<T> implements FileProcessingService<T> {
    private static final int MAX_BUFFER_SIZE = 100;
    private static final int BATCH_SIZE = 1000;
    private static final int BATCH_TIMEOUT = 100;

    @Value("${file.processing.parallelism:4}")
    private int fileProcessingParallelism;
    @Value("${file.io.parallelism:8}")
    private int fileIOParallelism;

    private final ActorSystem system;
    private ActorMaterializer actorMaterializer;
    private final FileDataProcessor<T> fileDataProcessor;
    private final BatchInsertSerivce batchInsertSerivce;

    public FileProcessingServiceImpl(final ActorSystem system, final FileDataProcessor<T> fileDataProcessor, final BatchInsertSerivce<T> batchInsertSerivce) {
        this.system = system;
        this.fileDataProcessor = fileDataProcessor;
        this.batchInsertSerivce = batchInsertSerivce;
    }

    @PostConstruct
    private void setupActors() {
        final ActorMaterializerSettings settings = ActorMaterializerSettings
                .create(this.system)
                .withInputBuffer(1, 16);
        this.actorMaterializer = ActorMaterializer.create(settings, this.system);
    }

    @Override
    public void processFile(final String filePath) {
        final PerformanceAction performanceAction = PerformanceFactory.startMeasurePerformance("File Processing");
        FileIO.fromPath(Paths.get(filePath))
                .via(Framing.delimiter(ByteString.fromString("\n"), 256, FramingTruncation.ALLOW))
                .drop(1)
                .buffer(MAX_BUFFER_SIZE, OverflowStrategy.backpressure())
                .mapAsyncUnordered(fileProcessingParallelism,
                        line -> CompletableFuture.supplyAsync(() -> parseByteStringToModel(line)))
                .groupedWithin(BATCH_SIZE, Duration.ofMillis(BATCH_TIMEOUT))
                .mapAsyncUnordered(fileIOParallelism, group -> CompletableFuture.supplyAsync(() -> batchInsertSerivce.batchInsert(group)))
                .recover(new PFBuilder().match(RuntimeException.class, ex -> "stream truncated: " + ex).build())
                .runForeach(line -> System.out.println(line), actorMaterializer)
                .whenComplete((cs, t) -> performanceAction.finishAction());
    }

    private T parseByteStringToModel(final ByteString byteString) {
        final String decodedString = ByteStringDecoder.decodeToStringWithoutWhitespace(byteString);
        return fileDataProcessor.processFileLine(decodedString);

    }
}
