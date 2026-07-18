# 🚚 Contenedores API - Sistema de Gestión Logística Integral

> Plataforma backend de gestión integral para operaciones logísticas de transporte de contenedores, desarrollada con arquitectura de microservicios.

---

## 📋 Descripción del Proyecto

**Contenedores API** es un sistema backend robusto y escalable diseñado para automatizar y optimizar la gestión del ciclo completo de operaciones logísticas: desde la solicitud de transporte de contenedores hasta la entrega final, pasando por la planificación de rutas, asignación de vehículos y seguimiento en tiempo real.

El proyecto implementa una arquitectura de **microservicios** basada en **Spring Boot 3.5**, con comunicación sincrona via **Feign Client** y un **API Gateway** centralizado que actúa como único punto de entrada, gestionando seguridad mediante **Keycloak**.

---

## 🎯 Funcionalidades Principales

### Gestión de Flota y Vehículos
- **Administración de camiones**: Registro, edición, soft-delete y cambio de disponibilidad.
- **Asignación de transportistas**: Vinculación dinámica de conductores a vehículos disponibles.
- **Cálculo de costos**: Determinación de costos base y promedio según características del vehículo y carga.

### Gestión de Contenedores
- **Registro de contenedores**: Alta de unidades con peso y volumen específico.
- **Asignación de clientes**: Relación contenedor-cliente con transición automática de estados.
- **Control de estados**: Máquina de estados finitos (Borrador → Programado → En Tránsito → En Depósito → Entregado).

### Solicitudes y Envíos
- **Gestión de solicitudes**: Creación, edición y seguimiento de pedidos de transporte.
- **Cálculo automático de tarifas**: Estimación de costos basada en distancia, consumo y estadías.
- **Workflow de estados**: Transiciones controladas con validaciones de negocio.

### Planificación de Rutas
- **Generación de rutas tentativas**: Cálculo automático de trayectos entre múltiples ubicaciones.
- **Integración con OSRM**: Servicio externo de enrutamiento para optimización de distancias y tiempos.
- **Asignación dinámica**: Distribución automática de tramos a vehículos disponibles.

### Seguimiento Operativo
- **Registro de inicio/fin de tramos**: Tracking del progreso operativo en tiempo real.
- **Control de transiciones**: Validación de secuencia operativa (no se puede iniciar un tramo sin finalizar el anterior).
- **Cálculo de costos reales**: Determinación de costo final basado en estadísticas reales de operación.

---

## 👥 ¿A quién está dirigido?

| Rol | Beneficio |
|-----|-----------|
| **Empresas de logística** | Automatización completa del ciclo operativo, reducción de errores manuales y optimización de recursos. |
| **Transportistas** | Información clara de asignaciones, estados de rutas y documentación de servicios. |
| **Clientes** | Tracking de sus envíos, transparencia en costos y tiempos estimados. |
| **Administradores** | Dashboard centralizado con logs estructurados y observabilidad completa del sistema. |

---

## 🛠️ Stack Tecnológico

| Componente | Tecnología |
|------------|------------|
| **Lenguaje** | Java 21 |
| **Framework** | Spring Boot 3.5 |
| **Arquitectura** | Microservicios (Spring Cloud) |
| **Gateway** | Spring Cloud Gateway |
| **Seguridad** | Keycloak (OAuth2 / OIDC) |
| **Comunicación** | Feign Client (REST) |
| **Base de Datos** | PostgreSQL |
| **Contenedores** | Docker & Docker Compose |
| *Logs* | Loki + Grafana |
| **Documentación** | OpenAPI (Swagger) |
| **Calidad** | SonarQube |

---

## 🏗️ Arquitectura

```
                              ┌─────────────────────┐
                              │   API Gateway        │
                              │  (Spring Cloud)      │
                              │  + Keycloak          │
                              └──────────┬──────────┘
                                         │
         ┌──────────────┬────────────────┼────────────────┬──────────────┐
         │              │                │                │              │
         ▼              ▼                ▼                ▼              ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│   Camiones    │ │  Contenedores │ │    Envíos     │ │    Tramos     │ │   Ubicaciones │
│   Service     │ │    Service    │ │    Service    │ │    Service    │ │    Service    │
└───────┬───────┘ └───────┬───────┘ └───────┬───────┘ └───────┬───────┘ └───────┬───────┘
        │                 │                 │                 │                 │
        └─────────────────┴─────────────────┴─────────────────┴─────────────────┘
                                         │
                              ┌──────────┴──────────┐
                              │   PostgreSQL        │
                              │   (Una DB por       │
                              │    microservicio)   │
                              └─────────────────────┘
```

### Microservicios

1. **Gateway Service**: Punto de entrada único, gestión de rutas y seguridad.
2. **Camiones Service**: Gestión de flota vehicular.
3. **Contenedores Service**: Administración de unidades de carga.
4. **Envíos Service**: Gestión de solicitudes y tarifas.
5. **Tramos Service**: Planificación de rutas y seguimiento operativo.
6. **Ubicaciones Service**: Gestión de depósitos y ubicaciones geográficas.
7. **Usuarios Service**: Gestión de usuarios y roles (integrado con Keycloak).

---

## 🚀 Configuración y Setup

### Requisitos Previos

- **Java 21** o superior
- **Docker** y **Docker Compose**
- **Maven** 3.9+

### Levantar el Proyecto

```bash
# 1. Clonar el repositorio
git clone <repo-url>
cd TPI-Backend

# 2. Iniciar servicios de infraestructura (PostgreSQL, Keycloak, Loki, Grafana)
docker-compose up -d

# 3. Compilar cada microservicio
mvn clean package -DskipTests

# 4. Ejecutar cada microservicio
# (Desde cada directorio de servicio)
java -jar target/*.jar
```

### Variables de Entorno

Cada microservicio cuenta con su propio `application.yml` configurado para desarrollo local. Los servicios de infraestructura se configuran automáticamente via Docker Compose.

### Puertos de Acceso

| Servicio | Puerto |
|----------|--------|
| Gateway | 8080 |
| Keycloak (Admin) | 8180 |
| PostgreSQL (General) | 5432 |
| Grafana | 3000 |
| Loki | 3100 |

### Documentación API

Una vez iniciado el Gateway, accedé a la documentación interactiva:

```
http://localhost:8080/swagger-ui.html
```

