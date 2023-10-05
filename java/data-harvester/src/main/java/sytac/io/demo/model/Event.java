package sytac.io.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String eventId;
    private String eventName;
    private String showId;
    private String cast;
    private Integer releaseYear;
    private String title;
    private String platform;
    private String eventDate;
}
