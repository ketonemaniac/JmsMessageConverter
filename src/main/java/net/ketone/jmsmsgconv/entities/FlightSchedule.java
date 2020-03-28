package net.ketone.jmsmsgconv.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.xml.annotate.JacksonXmlProperty;
import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "flightSchedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSchedule {

    private Long timestamp;

    @JacksonXmlProperty
    @JsonProperty("flight_no")
    private String flightNo;

}
