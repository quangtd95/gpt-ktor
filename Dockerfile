FROM gradle:8-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN --mount=type=cache,target=/root/.gradle  gradle buildFatJar --no-daemon

FROM openjdk:17
EXPOSE 8989
RUN mkdir /app
COPY --from=build /home/gradle/src/app/build/libs/*.jar /app/app-all.jar
ENTRYPOINT ["java","-jar","/app/app-all.jar"]