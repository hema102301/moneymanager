FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/money_manager.jar moneymanager-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "moneymanager-v1.0.jar"]

