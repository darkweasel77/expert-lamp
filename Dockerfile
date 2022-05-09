FROM gradle:jdk11-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM amazoncorretto:11-alpine
EXPOSE 8080
RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/expert-lamp.jar /app/expert-lamp.jar 

ENTRYPOINT ["java", "-jar", "/app/expert-lamp.jar"]
