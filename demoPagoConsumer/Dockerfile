FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} appConsumer.jar
ENTRYPOINT ["java", "-jar", "/appConsumer.jar"]