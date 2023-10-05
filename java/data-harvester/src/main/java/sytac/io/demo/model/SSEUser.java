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
public class SSEUser {
    private String id;
    @JsonProperty("date_of_birth")
    private String dateOfBirth;
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    private String gender;
    @JsonProperty("ip_address")
    private String ipAddress;
    private String country;
    @JsonProperty("last_name")
    private String lastName;

}
