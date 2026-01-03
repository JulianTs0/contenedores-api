FROM maven:3.9.6-eclipse-temurin-21 AS app_builder
WORKDIR /build

COPY gateway/pom.xml ./pom.xml
RUN mvn dependency:go-offline

COPY gateway/src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=app_builder /build/target/gateway-0.0.1-SNAPSHOT.jar app.jar

RUN mkdir -p /app/logs

VOLUME /app/logs

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
