FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.10_9
VOLUME /tmp
ARG JAR_FILE
ADD /target/user-0.0.1-SNAPSHOT.jar user-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","user-0.0.1-SNAPSHOT.jar"]
EXPOSE 8081