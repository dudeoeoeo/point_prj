FROM amazoncorretto:11-alpine3.13

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x ./gradlew && ./gradlew clean build

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} *.jar

ENTRYPOINT ["java", "-jar", "*.jar"]

EXPOSE 8080