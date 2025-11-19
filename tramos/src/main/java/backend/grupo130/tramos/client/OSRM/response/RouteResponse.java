package backend.grupo130.tramos.client.OSRM.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RouteResponse {

    @JsonProperty("geometry")
    private String geometry;

    @JsonProperty("distance")
    private double distance;

    @JsonProperty("duration")
    private double duration;

}
