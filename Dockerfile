FROM amazoncorretto:17-alpine3.19 as builder
WORKDIR /app
ADD ./posttest/. .
RUN ["./gradlew","bootJar"]

# stage-2 running image
FROM gcr.io/distroless/java17-debian12:latest
WORKDIR /app
COPY --from=builder /app/build/libs/posttest-0.0.1-SNAPSHOT.jar posttest-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "posttest-0.0.1-SNAPSHOT.jar"]