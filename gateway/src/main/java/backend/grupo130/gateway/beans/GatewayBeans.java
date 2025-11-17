package backend.grupo130.gateway.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayBeans {

    @Bean
    public RouteLocator configuradorDeRutas(
        RouteLocatorBuilder builder,
        @Value("${server.uri.usuarios}") String uriUsuarios,
        @Value("${server.uri.contenedores}") String uriContenedores,
        @Value("${server.uri.tramos}") String uriTramos,
        @Value("${server.uri.camiones}") String uriCamiones,
        @Value("${server.uri.ubicaciones}") String uriUbicaciones,
        @Value("${server.uri.envios}") String uriEnvios
    )
    {
        return builder.routes()
            .route(r -> r
                .path("/api/usuarios/**")
                .uri(uriUsuarios)
            )
            .route(r -> r
                .path("/api/contenedores/**")
                .uri(uriContenedores)
            )
            .route(r -> r
                .path("/api/tramos/**")
                .uri(uriTramos)
            )
            .route(r -> r
                .path("/api/camiones/**")
                .uri(uriCamiones)
            )
            .route(r -> r
                .path("/api/ubicaciones/**")
                .uri(uriUbicaciones)
            )
            .route(r -> r
                .path("/api/envios/**")
                .uri(uriEnvios)
            )
            .build();
    }

}
