package sytac.io.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SSEShow {
    @JsonProperty("show_id")
    private String showId;
    private String cast;
    private String country;
    @JsonProperty("data_added")
    private String dataAdded;
    private String description;
    private String director;
    private String duration;
    @JsonProperty("listed_in")
    private String listedIn;
    private String rating;
    @JsonProperty("release_year")
    private Integer releaseYear;
    private String title;
    private String type;
    private String platform;
}
