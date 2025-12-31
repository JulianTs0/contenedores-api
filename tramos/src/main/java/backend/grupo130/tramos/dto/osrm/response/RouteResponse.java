package backend.grupo130.tramos.dto.osrm.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RouteResponse {

    @JsonProperty("geometry")
    private final String geometry;

    @JsonProperty("distance")
    private final double distance;

    @JsonProperty("duration")
    private final double duration;

}
