package sytac.io.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import sytac.io.demo.config.AppProperties;
import sytac.io.demo.model.StreamResponseStatistics;
import sytac.io.demo.util.TestDataMaker;

import java.io.IOException;

@SpringBootTest
public class DataHarvesterImplTest {
    public static MockWebServer mockBackEnd;
    @Mock
    private AppProperties appProperties;
    private DataHarvesterImpl dataHarvester;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TestDataMaker testDataMaker = new TestDataMaker();
    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }
    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
        Mockito.when(appProperties.getStreamingHost()).thenReturn(baseUrl);
        Mockito.when(appProperties.getSytflix()).thenReturn("/sytflix");
        Mockito.when(appProperties.getSytazon()).thenReturn("/sytazon");
        Mockito.when(appProperties.getSysney()).thenReturn("/sysney");
        Mockito.when(appProperties.getDuration()).thenReturn(20);
        Mockito.when(appProperties.getUsername()).thenReturn("uname123");
        Mockito.when(appProperties.getPassword()).thenReturn("pass123");
        dataHarvester = new DataHarvesterImpl(appProperties);
    }

    @Test
    void testRetrieveDataFromStreamsSuccessful() throws JsonProcessingException {
                // Configure your mock objects

        StreamResponseStatistics responseMock = testDataMaker.createExpectedResponse();
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(responseMock))
                .setResponseCode(200)
                .addHeader("Content-Type", MediaType.TEXT_EVENT_STREAM));

        Mono<StreamResponseStatistics> stats = dataHarvester.retrieveDataFromStreams();

        StepVerifier.create(stats)
                .expectNextMatches(statistics -> statistics.getId()!=null
                    && statistics.getPercentageStartedStreamingOnSytflix()!= null
                    && statistics.getTotalStreamingDurationMillis()!=null
                    && statistics.getStreamingData() != null)
                .verifyComplete();

        RecordedRequest recordedRequest;
        try {
            recordedRequest = mockBackEnd.takeRequest();
            Assertions.assertEquals("GET", recordedRequest.getMethod());
            Assertions.assertNotEquals(null, recordedRequest.getBody());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRetrieveDataFromStreamsWithError() {
        // Set up a mock response with an error status code
        mockBackEnd.enqueue(new MockResponse().setResponseCode(500));
        try {
            // Shutdown the streams
            mockBackEnd.shutdown();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Make a request to the mock server
        Mono<StreamResponseStatistics> result = dataHarvester.retrieveDataFromStreams();

        StepVerifier.create(result)
                .expectError(RuntimeException.class) // Expecting an error
                .verify();
    }

}
