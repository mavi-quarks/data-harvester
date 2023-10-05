package sytac.io.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import sytac.io.demo.config.AppProperties;
import sytac.io.demo.model.SSEMessage;
import sytac.io.demo.model.SSEData;
import sytac.io.demo.model.StreamResponseStatistics;
import sytac.io.demo.util.Helper;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DataHarvesterImpl implements DataHarvesterIF {
    private final static Logger logger = LoggerFactory.getLogger(DataHarvesterImpl.class);
    private final AppProperties config;
    private final WebClient webClient;
    private final AtomicInteger sytacCounter = new AtomicInteger(0);

    @Autowired
    public DataHarvesterImpl(AppProperties appProperties)
    {
        this.config = appProperties;
        this.webClient = WebClient.create(config.getStreamingHost());
    }

    @Override
    public Mono<StreamResponseStatistics> retrieveDataFromStreams() {
        logger.info("getAllData: " + config.toString());

        // Fetch data from three different streams
        Flux<ServerSentEvent<String>> sytflixStream= fetchDataFromStream(config.getSytflix());
        Flux<ServerSentEvent<String>> sytazonStream = fetchDataFromStream(config.getSytazon());
        Flux<ServerSentEvent<String>> sysneyStream = fetchDataFromStream(config.getSysney());

        long startTime = System.currentTimeMillis();
        
        Flux<SSEMessage> streamingResults = Flux.merge(sysneyStream, sytflixStream, sytazonStream)
                .map( item -> SSEMessage.builder()
                        .id(item.id())
                        .event(item.event())
                        .data(Helper.parseStringToShowEvent(item.data()))
                        .build())
                .filter(Helper::isValidMyData) // Implement schema validation logic to skip malformed data
                .takeWhile(item -> shouldContinueCollecting(item.getData())) // Ensures it stops after the 3rd occurrence of 'Sytac'
                .take(Duration.ofSeconds(config.getDuration())) // or when 20 secs have passed
                .retryWhen(Retry.backoff(3, Duration.ofMillis(15))) // Configure to be fault-tolerant with retry and error handling
                .onErrorResume(error -> {
                    logger.error("Error: " + error);
                    return Mono.error(new RuntimeException(error));
                });

        return streamingResults
                .collectList()
                .map(myDataList -> {
                    long endTime = System.currentTimeMillis();
                    long timeElapsed = endTime - startTime;

                    return StreamResponseStatistics.builder()
                            .id(UUID.randomUUID().toString())
                            .totalShowsReleasedIn2020AndLater(Helper.countShowsReleasedAfterThreshold(myDataList))
                            .totalStreamingDurationMillis(timeElapsed)
                            .percentageStartedStreamingOnSytflix(Helper.calculatePercentageStartedSytflix(myDataList))
                            .totalNumberOfEvents(myDataList.size())
                            .streamingData(Helper.convertSSEMsgToStreamDataList(myDataList))
                            .build();
                });
    }

    private Flux<ServerSentEvent<String>> fetchDataFromStream(String streamPath) {
        ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<>() {};
        return webClient.get()
                .uri(streamPath)
                .headers(headers -> headers.setBasicAuth(config.getUsername(), config.getPassword())) // Streaming platform's username and password
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(type);
    }

    private boolean shouldContinueCollecting(SSEData dataRecord) {
        if (config.getFirstNameSytac().equals(dataRecord.getUser().getFirstName())) {
            logger.info("sytac_id=" + dataRecord.getUser().getId());
            int count = sytacCounter.incrementAndGet();
            return count <= 3;   // Returns true when Sytac first name has been detected 3x already
        }
        return true;
    }
}
