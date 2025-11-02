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

    /**
     * Define la cadena de filtros de seguridad para el API Gateway.
     * Esta función configura las reglas de autorización para todos los endpoints expuestos
     * y habilita la validación de tokens JWT (Resource Server).
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(exchanges -> exchanges

                // RUTA PÚBLICA PARA REGISTRO: Permite el acceso sin autenticación para crear nuevos usuarios.
                .pathMatchers( "/api/login/oauth2/code/keycloak")
                .permitAll()

                // RUTA PROTEGIDA (Usuarios Genéricos): Requiere autenticación y CUALQUIERA de los roles del TPI.
                // Roles permitidos: Administrador, Cliente o Transportista.
                // Nota: hasAnyRole() espera los roles sin el prefijo "ROLE_"
                .pathMatchers("/gateway/usuarios/**")
                .hasAnyRole("ADMINISTRADOR", "CLIENTE", "TRANSPORTISTA")

                .anyExchange().authenticated()
            )

            // Configura el Resource Server para procesar tokens JWT
            .oauth2ResourceServer(oauth2 -> oauth2
                // Conecta la lógica de validación del JWT, usando el conversor de roles
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )

            // Deshabilita la protección CSRF (Cross-Site Request Forgery), práctica común en APIs sin estado (stateless)
            .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }

    /**
     * Define el conversor de tokens JWT (JSON Web Token) a un objeto de autenticación
     * que Spring Security pueda entender y utilizar en el contexto de un Resource Server reactivo.
     *
     * Esta configuración es esencial para el API Gateway (Resource Server) [3] al permitirle:
     * 1. Extraer los roles del usuario desde el 'claim' de acceso al Realm ('realm_access').
     * 2. Mapear esos roles al formato requerido por Spring Security (prefiando con 'ROLE_').
     * 3. Proporcionar un objeto de autenticación (AbstractAuthenticationToken) para la fase de autorización.
     */
    private ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        // Inicializa el conversor base de JWT a Authentication
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        // Configura la lógica para extraer los permisos (Granted Authorities) del JWT.
        // Este conversor se enfoca en el mapeo de los roles definidos en Keycloak (tpi-backend).
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Accede al 'claim' 'realm_access' del token JWT. Este 'claim' es donde Keycloak
            // almacena la información de los roles del Realm asociados al usuario [8].
            Object realmAccess = jwt.getClaim("realm_access");

            if (realmAccess instanceof Map) {

                Map<?, ?> realmMap = (Map<?, ?>) realmAccess;
                // Se espera que la lista de roles esté bajo la clave "roles" dentro de 'realm_access' [8].
                Object roles = realmMap.get("roles");

                if (roles instanceof Collection) {

                    Collection<?> lst = (Collection<?>) roles;
                    return lst.stream()
                        .map(Object::toString)
                        // Transformación crítica: Cada rol se convierte a mayúsculas y se le antepone el prefijo
                        // "ROLE_" (Ej: 'ADMINISTRADOR' -> 'ROLE_ADMINISTRADOR'). Spring Security requiere
                        // este formato para su mecanismo de autorización basado en roles (.hasRole(), .hasAnyRole()) [8].
                        .map(r -> "ROLE_" + r.toUpperCase())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                }
            }
            // Si no se encuentra el claim o no contiene roles, se devuelve una lista vacía de autoridades.
            return Collections.emptyList();
        });

        // Envuelve el conversor síncrono en un adaptador reactivo (ReactiveJwtAuthenticationConverterAdapter).
        // Esto es necesario debido a que Spring Cloud Gateway utiliza Spring WebFlux [6, 7],
        // y la cadena de filtros de seguridad requiere un resultado de tipo Mono<AbstractAuthenticationToken> [7].
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

}
