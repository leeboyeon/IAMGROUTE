FROM openjdk:8-jdk-alpine
WORKDIR /server/groute/
#ARG JAR_FILE=build/libs/groute-0.0.1-SNAPSHOT.jar
ARG JAR_FILE=/groute-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
