FROM openjdk:11
EXPOSE 8989
RUN mkdir /app
COPY  /app/build/libs/app-all.jar /app/app-all.jar
ENTRYPOINT ["java","-jar","/app/app-all.jar"]