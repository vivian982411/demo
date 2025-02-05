FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} appPayment.jar
ENTRYPOINT ["java", "-jar", "/appPayment.jar"]