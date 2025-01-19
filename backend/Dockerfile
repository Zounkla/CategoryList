FROM maven:3.8-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package


FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /app/target/TP1-0.0.1-SNAPSHOT.jar categoryList.jar
ENTRYPOINT ["java","-jar","categoryList.jar"]
EXPOSE 8080
