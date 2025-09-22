# Stage 1: Build with Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/money_manager.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","app.jar"]
