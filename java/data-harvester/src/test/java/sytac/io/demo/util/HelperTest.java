package sytac.io.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sytac.io.demo.model.SSEData;
import sytac.io.demo.model.SSEMessageList;
import sytac.io.demo.model.StreamResponseStatistics;
import sytac.io.demo.model.StreamingData;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HelperTest {
    final TestDataMaker testDataMaker = new TestDataMaker();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void countSuccessfulStreamingEvents() {
        try {
            String eventListFile = "src/main/resources/templates/test/EventList_SuccessfulOne.json";
            StreamResponseStatistics statistics = objectMapper.readValue(new File(eventListFile), StreamResponseStatistics.class);
            int countActual = Helper.countSuccessfulStreamingEvents(statistics.getStreamingData().get(0).getEventList());
            assertEquals((int) statistics.getStreamingData().get(0).getSuccessfulEvents(), countActual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void countSuccessfulStreamZero() {
        try {
            String eventListFile = "src/main/resources/templates/test/EventList_SuccessfulZero.json";
            StreamResponseStatistics statistics = objectMapper.readValue(new File(eventListFile), StreamResponseStatistics.class);
            int countActual = Helper.countSuccessfulStreamingEvents(statistics.getStreamingData().get(0).getEventList());
            assertEquals((int) statistics.getStreamingData().get(0).getSuccessfulEvents(), countActual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void countSuccessfulStreamZeroEventInBetween() {
        try {
            String eventListFile = "src/main/resources/templates/test/EventList_Successful_EventInBetween.json";
            StreamResponseStatistics statistics = objectMapper.readValue(new File(eventListFile), StreamResponseStatistics.class);
            int countActual = Helper.countSuccessfulStreamingEvents(statistics.getStreamingData().get(0).getEventList());
            assertEquals((int) statistics.getStreamingData().get(0).getSuccessfulEvents(), countActual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void countShowsReleasedAfterThreshold() {
        try {
            String eventListFile = "src/main/resources/templates/test/SSEMessageList.json";
            String statsResponseFile = "src/main/resources/templates/test/StreamStatistics.json";
            SSEMessageList sseMessageList = objectMapper.readValue(new File(eventListFile), SSEMessageList.class);
            StreamResponseStatistics statsResponse = objectMapper.readValue(new File(statsResponseFile), StreamResponseStatistics.class);

            long countActual = Helper.countShowsReleasedAfterThreshold(sseMessageList.getServerMessages());
            assertEquals((long) statsResponse.getTotalShowsReleasedIn2020AndLater(), countActual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void calculatePercentageStartedSytflix() {
        try {
            String eventListFile = "src/main/resources/templates/test/SSEMessageList.json";
            String statsResponseFile = "src/main/resources/templates/test/StreamStatistics.json";
            SSEMessageList sseMessageList = objectMapper.readValue(new File(eventListFile), SSEMessageList.class);
            StreamResponseStatistics statsResponse = objectMapper.readValue(new File(statsResponseFile), StreamResponseStatistics.class);

            double percentActual = Helper.calculatePercentageStartedSytflix(sseMessageList.getServerMessages());
            assertEquals(statsResponse.getPercentageStartedStreamingOnSytflix(), percentActual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void isValidMyData() {
        try {
            String eventListFile = "src/main/resources/templates/test/SSEMessageList.json";
            SSEMessageList sseMessageList = objectMapper.readValue(new File(eventListFile), SSEMessageList.class);

            boolean valid = Helper.isValidMyData(sseMessageList.getServerMessages().get(0));
            assertTrue(valid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void parseStringToShowEvent() {
        try {
            String eventListFile = "src/main/resources/templates/test/SSEMessageList.json";
            SSEMessageList sseMessageList = objectMapper.readValue(new File(eventListFile), SSEMessageList.class);
            String jsonSseData = objectMapper.writeValueAsString(sseMessageList.getServerMessages().get(0).getData());
            SSEData sseData = Helper.parseStringToShowEvent(jsonSseData);
            Assertions.assertEquals(sseMessageList.getServerMessages().get(0).getData(), sseData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void convertSSEMsgToStreamDataList() {
        try {
            String eventListFile = "src/main/resources/templates/test/SSEMessageList.json";
            String statsResponseFile = "src/main/resources/templates/test/StreamStatistics.json";
            SSEMessageList sseMessageList = objectMapper.readValue(new File(eventListFile), SSEMessageList.class);
            StreamResponseStatistics statsResponse = objectMapper.readValue(new File(statsResponseFile), StreamResponseStatistics.class);

            List<StreamingData> dataList = Helper.convertSSEMsgToStreamDataList(sseMessageList.getServerMessages());
            Assertions.assertEquals(statsResponse.getStreamingData().size(), dataList.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void convertToShowDataList() {
        List<StreamingData> streaming =  Helper.convertSSEMsgToStreamDataList(testDataMaker.createTestData());
        // The number of unique users_id is equal to the size of streaming
        long numUsers = streaming.stream()
                .collect(Collectors.groupingBy(StreamingData::getUserId))
                .values()
                .size();
        Assertions.assertEquals(numUsers, streaming.size());
    }

}