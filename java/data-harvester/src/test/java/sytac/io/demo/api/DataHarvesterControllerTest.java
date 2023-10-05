package sytac.io.demo.api;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import sytac.io.demo.model.StreamResponseStatistics;
import sytac.io.demo.service.DataHarvesterImpl;
import sytac.io.demo.util.TestDataMaker;

@WebFluxTest(DataHarvesterController.class)
class DataHarvesterControllerTest {
    @MockBean
    private DataHarvesterImpl dataHarvester;

    @Autowired
    private WebTestClient webTestClient;

    private final TestDataMaker testDataMaker = new TestDataMaker();

    @Test
    public void testCollectData() {
        // Define expected response
        StreamResponseStatistics expectedResponse = testDataMaker.createExpectedResponse();

        // Mock dataHarvester behavior
        Mockito.when(dataHarvester.retrieveDataFromStreams()).thenReturn(Mono.just(expectedResponse));

        // Send a GET request to the collectData() endpoint and verify the response
        webTestClient.get()
                .uri("/dhm/v1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(StreamResponseStatistics.class)
                .isEqualTo(expectedResponse);
    }
}