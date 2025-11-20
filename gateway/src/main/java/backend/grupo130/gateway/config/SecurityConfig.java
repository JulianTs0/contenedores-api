package backend.grupo130.gateway.config;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.web.server.ServerHttpSecurity;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        final String ROL_ADMIN = "ADMINISTRADOR";
        final String ROL_CLIENTE = "CLIENTE";
        final String ROL_TRANSP = "TRANSPORTISTA";

        http
            .authorizeExchange(exchanges -> exchanges

                .pathMatchers( "/api/login/oauth2/code/keycloak")
                .permitAll()

                .pathMatchers("/v3/api-docs/**").permitAll()
                .pathMatchers("/swagger-ui/**").permitAll()
                .pathMatchers("/swagger-ui.html").permitAll()

                .pathMatchers("/gateway/usuarios/**")
                .hasAnyRole(ROL_ADMIN, ROL_CLIENTE, ROL_TRANSP)

                .pathMatchers(HttpMethod.GET, "/gateway/camiones/getById/{dominio}")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.GET, "/gateway/camiones/getAll")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/gateway/camiones/getDisponibles")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/gateway/camiones/register")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/gateway/camiones/edit")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/gateway/camiones/asignarTransportista")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/gateway/camiones/delete/{dominio}")
                .hasAnyRole(ROL_ADMIN)

                .pathMatchers(HttpMethod.GET, "/gateway/contenedores/getById/{id}")
                .hasAnyRole(ROL_ADMIN, ROL_CLIENTE)
                .pathMatchers(HttpMethod.GET, "/gateway/contenedores/getByEstado/{estado}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/gateway/contenedores/getAll")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/gateway/contenedores/register")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/gateway/contenedores/edit")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/gateway/contenedores/delete/{id}")
                .hasAnyRole(ROL_ADMIN)

                .pathMatchers(HttpMethod.GET, "/gateway/envios/solicitud/getById/{id}")
                .hasAnyRole(ROL_ADMIN, ROL_CLIENTE)
                .pathMatchers(HttpMethod.GET, "/gateway/envios/solicitud/getAll")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/gateway/envios/solicitud/register")
                .hasAnyRole(ROL_CLIENTE)
                .pathMatchers(HttpMethod.PATCH, "/gateway/envios/solicitud/confirmarSolicitud")
                .hasAnyRole(ROL_CLIENTE)


                .pathMatchers(HttpMethod.GET, "/gateway/tramos/rutas/getById/{idRuta}")
                .hasAnyRole(ROL_ADMIN, ROL_CLIENTE, ROL_TRANSP)
                .pathMatchers(HttpMethod.GET, "/gateway/tramos/rutas/getAll")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.POST, "/gateway/tramos/rutas/crearRutaTentativa")
                .hasAnyRole(ROL_CLIENTE)

                .pathMatchers(HttpMethod.GET, "/gateway/tramos/tramos/getById/{idTramo}")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.GET, "/gateway/tramos/tramos/getAll")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.PATCH, "/gateway/tramos/tramos/asignarCamion")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/gateway/tramos/tramos/getByTransportista/{dominio}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/gateway/tramos/tramos/getByRuta/{idRuta}")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.PATCH, "/gateway/tramos/tramos/registrarInicio")
                .hasAnyRole(ROL_TRANSP)
                .pathMatchers(HttpMethod.PATCH, "/gateway/tramos/tramos/registrarFin")
                .hasAnyRole(ROL_TRANSP)


                .pathMatchers(HttpMethod.GET, "/gateway/ubicaciones/ubicaciones/getById/{id}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/gateway/ubicaciones/ubicaciones/getAll")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/gateway/ubicaciones/ubicaciones/register")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/gateway/ubicaciones/ubicaciones/edit")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/gateway/ubicaciones/ubicaciones/delete/{id}")
                .hasAnyRole(ROL_ADMIN)

                .pathMatchers(HttpMethod.GET, "/gateway/ubicaciones/depositos/getById/{idDeposito}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/gateway/ubicaciones/depositos/getAll")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/gateway/ubicaciones/depositos/register")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/gateway/ubicaciones/depositos/edit")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/gateway/ubicaciones/depositos/delete/{id}")
                .hasAnyRole(ROL_ADMIN)

                .anyExchange().authenticated()
            )

            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )

            .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }

    private ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();


        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            Object realmAccess = jwt.getClaim("realm_access");

            if (realmAccess instanceof Map) {

                Map<?, ?> realmMap = (Map<?, ?>) realmAccess;
                Object roles = realmMap.get("roles");

                if (roles instanceof Collection) {

                    Collection<?> lst = (Collection<?>) roles;
                    return lst.stream()
                        .map(Object::toString)

                        .map(r -> "ROLE_" + r.toUpperCase())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                }
            }
            return Collections.emptyList();
        });

        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

}
