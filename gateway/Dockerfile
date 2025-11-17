# Etapa 1: Compilación con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS app_builder
WORKDIR /build

# Copiar el pom.xml del módulo
COPY gateway/pom.xml ./pom.xml
# Descargar dependencias
RUN mvn dependency:go-offline

# Copiar el código fuente del módulo
COPY gateway/src ./src
# Compilar la aplicación
RUN mvn clean install -DskipTests

# Etapa 2: Imagen de ejecución (solo JRE)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar el JAR compilado de la etapa anterior
COPY --from=app_builder /build/target/gateway-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto
EXPOSE 8082

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]
