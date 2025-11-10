package backend.grupo130.tramos.client.OSRM.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OsrmRouteResponse {

    @JsonProperty("code")
    private String code;

    @JsonProperty("routes")
    private List<RouteResponse> routes;

}
