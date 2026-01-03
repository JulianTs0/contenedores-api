package backend.grupo130.gateway.config;

import backend.grupo130.gateway.config.enums.Rol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        final String ROL_ADMIN = Rol.ADMINISTRADOR.name();
        final String ROL_CLIENTE = Rol.CLIENTE.name();
        final String ROL_TRANSP = Rol.TRANSPORTISTA.name();

        http
            .cors(
                cors -> cors.configurationSource(this.corsConfigurationSource())
            )
            .authorizeExchange(exchanges -> exchanges

                .pathMatchers( "/api/login/oauth2/code/keycloak")
                .permitAll()

                .pathMatchers("/v3/api-docs/**")
                .permitAll()
                .pathMatchers("/swagger-ui/**")
                .permitAll()
                .pathMatchers("/swagger-ui.html")
                .permitAll()

                .pathMatchers(HttpMethod.GET, "/api/camiones/{dominio}")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.GET, "/api/camiones/")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.GET, "/api/camiones/disponibles")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/camiones/")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/camiones/{dominio}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/camiones/disponibilidad/{dominio}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/camiones/transportista/{dominio}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/api/camiones/{dominio}")
                .hasAnyRole(ROL_ADMIN)

                .pathMatchers(HttpMethod.GET, "/api/contenedores/{id}")
                .hasAnyRole(ROL_ADMIN, ROL_CLIENTE)
                .pathMatchers(HttpMethod.GET, "/api/contenedores/estado/{estado}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/api/contenedores/")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/contenedores/")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/contenedores/{id}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/contenedores/estado/{id}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/contenedores/cliente/{id}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/api/contenedores/{id}")
                .hasAnyRole(ROL_ADMIN)

                .pathMatchers(HttpMethod.GET, "/api/envios/precios/{id}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/api/envios/precios/")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/envios/precios/")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/envios/precios/{id}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/api/envios/precios/{id}")
                .hasAnyRole(ROL_ADMIN)

                .pathMatchers(HttpMethod.GET, "/api/envios/solicitud/{id}")
                .hasAnyRole(ROL_ADMIN, ROL_CLIENTE)
                .pathMatchers(HttpMethod.GET, "/api/envios/solicitud/")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/envios/solicitud/")
                .hasAnyRole(ROL_ADMIN, ROL_CLIENTE)
                .pathMatchers(HttpMethod.PATCH, "/api/envios/solicitud/{id}/confirmar")
                .hasAnyRole(ROL_ADMIN)

                .pathMatchers(HttpMethod.GET, "/api/tramos/rutas/{id}")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.GET, "/api/tramos/rutas/")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.POST, "/api/tramos/rutas/")
                .hasAnyRole(ROL_ADMIN, ROL_CLIENTE)

                .pathMatchers(HttpMethod.GET, "/api/tramos/tramos/{id}")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.GET, "/api/tramos/tramos/")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.PATCH, "/api/tramos/tramos/camion/{id}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/api/tramos/tramos/transportista/{dominio}")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.GET, "/api/tramos/tramos/ruta/{id}")
                .hasAnyRole(ROL_ADMIN, ROL_TRANSP)
                .pathMatchers(HttpMethod.PATCH, "/api/tramos/tramos/iniciar/{id}")
                .hasAnyRole(ROL_TRANSP)
                .pathMatchers(HttpMethod.PATCH, "/api/tramos/tramos/finalizar/{id}")
                .hasAnyRole(ROL_TRANSP)

                .pathMatchers(HttpMethod.GET, "/api/ubicaciones/depositos/{id}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/api/ubicaciones/depositos/")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/ubicaciones/depositos/")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/ubicaciones/depositos/{id}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/api/ubicaciones/depositos/{id}")
                .hasAnyRole(ROL_ADMIN)

                .pathMatchers(HttpMethod.GET, "/api/ubicaciones/ubicaciones/{id}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.GET, "/api/ubicaciones/ubicaciones/")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/ubicaciones/ubicaciones/")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.PATCH, "/api/ubicaciones/ubicaciones/{id}")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.DELETE, "/api/ubicaciones/ubicaciones/{id}")
                .hasAnyRole(ROL_ADMIN)

                .pathMatchers(HttpMethod.GET, "/api/usuarios/{id}")
                .hasAnyRole(ROL_ADMIN, ROL_CLIENTE, ROL_TRANSP)
                .pathMatchers(HttpMethod.GET, "/api/usuarios/")
                .hasAnyRole(ROL_ADMIN)
                .pathMatchers(HttpMethod.POST, "/api/usuarios/")
                .permitAll()
                .pathMatchers(HttpMethod.PATCH, "/api/usuarios/{id}")
                .hasAnyRole(ROL_ADMIN, ROL_CLIENTE, ROL_TRANSP)
                .pathMatchers(HttpMethod.DELETE, "/api/usuarios/{id}")
                .hasAnyRole(ROL_ADMIN, ROL_CLIENTE, ROL_TRANSP)

                .anyExchange().authenticated()
            )

            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )

            .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Collections.singletonList("*"));

        configuration.setAllowedMethods(
            Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        );
        configuration.setAllowedHeaders(
            Arrays.asList("Authorization", "Content-Type", "Cache-Control")
        );
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
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
