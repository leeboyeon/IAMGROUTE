FROM openjdk:8-jdk-alpine
WORKDIR /server/groute/
ADD ./build/libs/groute-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
