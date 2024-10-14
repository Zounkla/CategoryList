FROM maven:3.8-openjdk-17-slim

WORKDIR /backend

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

COPY src ./src

EXPOSE 8080

CMD mvn spring-boot:run