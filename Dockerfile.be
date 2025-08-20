FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /workspace
COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean bootJar -x test

FROM eclipse-temurin:17-jre-alpine as runtime

WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]