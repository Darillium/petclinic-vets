FROM openjdk:8-jdk-alpine
MAINTAINER Maxim Avezbakiev <maxim@darillium.io>
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
EXPOSE 9966
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dserver.port=9966","-jar","/app.jar"]
