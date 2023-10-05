package sytac.io.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreamResponseStatistics {
    private String id = UUID.randomUUID().toString();
    private Long totalShowsReleasedIn2020AndLater;
    private Long totalStreamingDurationMillis = 0L;
    private Double percentageStartedStreamingOnSytflix =0.0;
    private Integer totalNumberOfEvents;
    private List<StreamingData> streamingData;
}
