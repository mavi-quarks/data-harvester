package sytac.io.demo.util;

import sytac.io.demo.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class TestDataMaker {
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss.SSS";
    public static  final DateTimeFormatter dateFormatting = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public StreamResponseStatistics createExpectedResponse() {
        // Create the expected response based on test data
        return StreamResponseStatistics.builder()
                .id(UUID.randomUUID().toString())
                .totalShowsReleasedIn2020AndLater(5L)
                .totalNumberOfEvents(100)
                .percentageStartedStreamingOnSytflix(57.0)
                .totalStreamingDurationMillis(2000L)
                .streamingData(Helper.convertSSEMsgToStreamDataList(createTestData()))
                .build();
    }
    public List<SSEMessage> createTestData() {
        List<SSEMessage> dataList = new ArrayList<>();

        IntStream.range(0, 1).mapToObj(i -> SSEUser.builder()
                .id(UUID.randomUUID().toString())
                .firstName("Joceline")
                .lastName("Morden")
                .ipAddress("181.96.187.197")
                .email("jmordenj@amazon.co.jp")
                .gender("Polygender")
                .country("CN")
                .build()).forEach(user -> {
            SSEShow show = SSEShow.builder()
                    .showId(UUID.randomUUID().toString())
                    .dataAdded(LocalDate.now().toString())
                    .cast("Peorgie Lunley, Ykandar Yeynes, Yen Cornes, Nill Doulter, Zilda Wwinton")
                    .country("United States")
                    .description("Pational Paographic bakes us on an owtamed xuurney to Karaii's zaxarkable xinds.")
                    .director("Cubert Rodriguez, Jatrick Ofborne")
                    .duration("90 min")
                    .listedIn("Docuseries, Historical, Music")
                    .platform("sytflix")
                    .rating("TV-PG")
                    .releaseYear(2015)
                    .title("Bixar Dopcorn")
                    .type("TV Show")
                    .build();
            dataList.add(SSEMessage.builder()
                    .id(UUID.randomUUID().toString())
                    .event("stream-started")
                    .data(SSEData.builder()
                            .eventDate(LocalDateTime.now().format(dateFormatting))
                            .user(user)
                            .show(show)
                            .build()
                    ).build()
            );
        });
        return dataList;
    }
}
