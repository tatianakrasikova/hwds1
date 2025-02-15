# Stage 1: Build application
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

RUN addgroup --system spring && adduser --system --ingroup spring spring \
    && mkdir -p /app \
    && chown spring:spring /app

USER spring:spring
WORKDIR /app

COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]