package sytac.io.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreamingData {
    private String userId;
    private String completeName;
    private Integer age;
    private Integer successfulEvents;
    private List<Event> eventList;
}
