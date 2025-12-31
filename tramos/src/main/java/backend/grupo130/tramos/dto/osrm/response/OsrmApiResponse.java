package backend.grupo130.tramos.dto.osrm.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OsrmApiResponse {

    @JsonProperty("code")
    private final String code;

    @JsonProperty("routes")
    private final List<RouteResponse> routes;

}
