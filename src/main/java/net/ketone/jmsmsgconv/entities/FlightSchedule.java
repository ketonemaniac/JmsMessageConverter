package net.ketone.jmsmsgconv.entities;

import com.fasterxml.jackson.xml.annotate.JacksonXmlProperty;
import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

@JacksonXmlRootElement(localName = "flightSchedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSchedule {

    private Long timestamp;

    private String source;

    @JacksonXmlProperty
    @JsonProperty("flight_no")
    private String flightNo;

}
