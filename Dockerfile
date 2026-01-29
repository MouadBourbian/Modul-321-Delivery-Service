# Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy Maven settings
COPY docker/settings.xml /root/.m2/settings.xml

# Build application
COPY pom.xml .
RUN --mount=type=secret,id=GITHUB_ACTOR \
    --mount=type=secret,id=GITHUB_TOKEN \
    export GITHUB_ACTOR=$(cat /run/secrets/GITHUB_ACTOR) && \
    export GITHUB_TOKEN=$(cat /run/secrets/GITHUB_TOKEN) && \
    mvn dependency:go-offline -B

COPY src ./src
RUN --mount=type=secret,id=GITHUB_ACTOR \
    --mount=type=secret,id=GITHUB_TOKEN \
    export GITHUB_ACTOR=$(cat /run/secrets/GITHUB_ACTOR) && \
    export GITHUB_TOKEN=$(cat /run/secrets/GITHUB_TOKEN) && \
    mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the jar file with wildcard to handle version changes
COPY --from=build /app/target/delivery-service-*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
