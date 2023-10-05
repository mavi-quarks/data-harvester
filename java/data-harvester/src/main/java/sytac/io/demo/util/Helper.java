package sytac.io.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sytac.io.demo.model.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class provides helping methods to perform data conversion and
 * also, calculations for DHM service.
 */
public class Helper {
    private final static Logger logger = LoggerFactory.getLogger(Helper.class);
    private static final String DOB_FORMAT = "dd/MM/yyyy";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Returns the count of successful streaming events.
     * A successful streaming event is:
     * 1. A successful streaming event is a subsequence of stream-started & stream-finished and there are no in between event between them,
     * 2. These events should have the same showId and platform
     */
    public static int countSuccessfulStreamingEvents(List<Event> streamingList) {
        return (int) IntStream.range(0, streamingList.size() - 1)
                .filter(i -> "stream-started".equals(streamingList.get(i).getEventName()) &&
                        "stream-finished".equals(streamingList.get(i + 1).getEventName()) &&
                        streamingList.get(i).getShowId().equals(streamingList.get(i + 1).getShowId()) &&
                        streamingList.get(i).getPlatform().equals(streamingList.get(i + 1).getPlatform()))
                .count();
    }

    /**
     * Count the shows released on or after 2020
     * @param showDataList List of OrgData containing ShowData
     * @return Number of shows released after 2020
     */
    public static long countShowsReleasedAfterThreshold(List<SSEMessage> showDataList) {
        return showDataList.stream()
                .map(SSEMessage::getData)
                .map(SSEData::getShow)
                .filter(show -> show.getReleaseYear() != null && show.getReleaseYear() >= 2020)
                .map(SSEShow::getShowId) // Each show has a unique ID
                .distinct()
                .count();
    }

    /**
     * Calculate the percentage of events that occurred on Sytflix
     * @param dataList List of OrgData
     * @return Percentage of Sytflix events with two decimal places
     */
    public static double calculatePercentageStartedSytflix(List<SSEMessage> dataList) {
        long totalEvents = dataList.size();
        long sytflixEvents = dataList.stream()
                .filter(data -> "Sytflix".equals(data.getData().getShow().getPlatform())
                        && "stream-started".equals(data.getEvent())
                )
                .count();

        if (totalEvents == 0) {
            return 0.0;
        }

        double percentage = ((double) sytflixEvents / totalEvents) * 100.0;
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(percentage));
    }

    /**
     * Check if OrgData record is valid
     * @param dataRecord OrgData record
     * @return True if valid, false otherwise
     */
    public static boolean isValidMyData(SSEMessage dataRecord) {
        return dataRecord != null
                && dataRecord.getId() != null
                && dataRecord.getEvent() != null
                && dataRecord.getData() != null
                && dataRecord.getData().getShow() != null
                && dataRecord.getData().getUser() != null
                && dataRecord.getData().getShow().getShowId() != null
                && dataRecord.getData().getUser().getId() != null
                && dataRecord.getData().getEventDate()!= null;
    }

    // Define a method to parse JSON string into OrgData
    public static SSEData parseStringToShowEvent(String eventData) {
         // You can use your preferred JSON parser
        try {
            return objectMapper.readValue(eventData, SSEData.class);
        } catch (JsonProcessingException e) {
            return new SSEData();
        }
    }

    /**
     * Converts a list of data objects to a list of StreamData objects.
     * @param sseMessageList The list of ShowData objects to convert.
     * @return The list of StreamData objects.
     */
    public static List<StreamingData> convertSSEMsgToStreamDataList(List<SSEMessage> sseMessageList) {
        return sseMessageList.stream()
                .collect(Collectors.groupingBy(it -> it.getData().getUser().getId()))
                .values()
                .stream()
                .map(Helper::convertSSEMsgToStreamData)
                .collect(Collectors.toList());
    }

    /**
     * Converts a List ShowData objects to a StreamData object.
     * @return The StreamData object.
     */
    private static StreamingData convertSSEMsgToStreamData(List<SSEMessage> sseMessageList) {
        String userId = sseMessageList.get(0).getData().getUser().getId();
        SSEUser user = sseMessageList.get(0).getData().getUser();
        List<Event> eventList = sseMessageList.stream()
                .map(Helper::createEventFromShowData)
                .sorted(Comparator.comparing(Event::getEventDate))
                .collect(Collectors.toList());

        return StreamingData.builder()
                .userId(userId)
                .completeName(user.getFirstName() + " " + user.getLastName())
                .age(calculateAge(user.getDateOfBirth()))
                .eventList(eventList)
                .successfulEvents(Helper.countSuccessfulStreamingEvents(eventList))
                .build();
    }

    /**
     * Creates an Event object from a ShowData object.
     * @param sseMessage The ShowData object.
     * @return The Event object.
     */
    private static Event createEventFromShowData(SSEMessage sseMessage) {
        SSEShow show = sseMessage.getData().getShow();
        String amsterdamTime = TimeZoneMapper.convertTimeToAmsterdamCET(sseMessage.getData().getEventDate(),
                sseMessage.getData().getUser().getCountry());
        return Event.builder()
                .eventId(sseMessage.getId())
                .eventName(sseMessage.getEvent())
                .eventDate(amsterdamTime)
                .showId(show.getShowId())
                .title(show.getTitle())
                .cast(getFirstCastOnly(show.getCast()))
                .releaseYear(show.getReleaseYear())
                .platform(show.getPlatform())
                .build();
    }

    /**
     * Returns the first cast in the list.
     * @param casts The casts listed with delimiter comma
     * @return The first cast if there is.
     */
    private static String getFirstCastOnly(String casts) {
        if (casts == null || casts.isEmpty()) {
            return "";
        }
        String[] castList = casts.split(",");
        return castList[0].trim();
    }

    /**
     * Calculates the age based on the date of birth. It returns -1 if parsing is not successful.
     * @param dateOfBirth The date of birth in the format "dd/MM/yyyy".
     * @return The age.
     */
    private static Integer calculateAge(String dateOfBirth) {
        try {
            LocalDate birthDate = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern(DOB_FORMAT));
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears();
        } catch (Exception e) {
            return -1;
        }
    }
}
