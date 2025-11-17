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

                // Permite el acceso público al endpoint de callback de Keycloak
                .pathMatchers( "/api/login/oauth2/code/keycloak")
                .permitAll()

                // Servicio de Usuarios (No se proveyó controller, se mantiene el wildcard)
                .pathMatchers("/api/usuarios/**")
                .hasAnyRole(ROL_ADMIN, ROL_CLIENTE, ROL_TRANSP)

                // CamionController (/api/camiones)
                .pathMatchers(HttpMethod.GET, "/api/camiones/getById/{dominio}").hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.GET, "/api/camiones/getAll").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/api/camiones/getDisponibles").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/camiones/register").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/camiones/edit").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/camiones/asignarTransportista").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/api/camiones/delete/{dominio}").hasAnyRole(ROL_ADMIN)

                // ContenedorController (/api/contenedores)
                .pathMatchers(HttpMethod.GET, "/api/contenedores/getById/{id}").hasAnyRole(ROL_ADMIN, ROL_CLIENTE, ROL_TRANSP)
                .pathMatchers(HttpMethod.POST, "/api/contenedores/getByPesoVolumen").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/api/contenedores/getByEstado/{estado}").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/api/contenedores/getAll").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/contenedores/register").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/contenedores/edit").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/api/contenedores/delete/{id}").hasAnyRole(ROL_ADMIN)

                // RutaController (/api/rutas)
                .pathMatchers(HttpMethod.GET, "/api/rutas/getById/{idRuta}").hasAnyRole(ROL_ADMIN, ROL_CLIENTE)
                .pathMatchers(HttpMethod.GET, "/api/rutas/getAll").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/rutas/getRutaTentativa").hasAnyRole(ROL_CLIENTE)
                .pathMatchers(HttpMethod.PATCH, "/api/rutas/asignarSolicitud").hasAnyRole(ROL_CLIENTE)

                // TramoController (/api/tramos)
                .pathMatchers(HttpMethod.GET, "/api/tramos/getById/{idTramo}").hasAnyRole(ROL_ADMIN, ROL_CLIENTE, ROL_TRANSP)
                .pathMatchers(HttpMethod.GET, "/api/tramos/getAll").hasAnyRole(ROL_ADMIN, ROL_CLIENTE, ROL_TRANSP)
                .pathMatchers(HttpMethod.PATCH, "/api/tramos/asignarCamion").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/api/tramos/getByTransportista/{dominio}").hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.GET, "/api/tramos/getByRuta/{idRuta}").hasAnyRole(ROL_ADMIN, ROL_CLIENTE, ROL_TRANSP)
                .pathMatchers(HttpMethod.PATCH, "/api/tramos/registrarInicio").hasAnyRole(ROL_TRANSP)
                .pathMatchers(HttpMethod.PATCH, "/api/tramos/registrarFin").hasAnyRole(ROL_TRANSP)

                // SolicitudController (/api/envios/solicitud)
                .pathMatchers(HttpMethod.GET, "/api/envios/solicitud/getById/{id}").hasAnyRole(ROL_CLIENTE)
                .pathMatchers(HttpMethod.GET, "/api/envios/solicitud/getAll").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/envios/solicitud/register").hasAnyRole(ROL_CLIENTE)

                // DepositoController (/api/ubicaciones/depositos)
                .pathMatchers(HttpMethod.GET, "/api/ubicaciones/depositos/getById/{idDeposito}").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/api/ubicaciones/depositos/getAll").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/ubicaciones/depositos/register").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/ubicaciones/depositos/edit").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/api/ubicaciones/depositos/delete/{id}").hasAnyRole(ROL_ADMIN)

                // UbicacionController (/api/ubicaciones/ubicaciones)
                .pathMatchers(HttpMethod.GET, "/api/ubicaciones/ubicaciones/getById/{id}").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/api/ubicaciones/ubicaciones/getAll").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/ubicaciones/ubicaciones/register").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/ubicaciones/ubicaciones/edit").hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/api/ubicaciones/ubicaciones/delete/{id}").hasAnyRole(ROL_ADMIN)

                // Cualquier otra solicitud debe estar autenticada
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
