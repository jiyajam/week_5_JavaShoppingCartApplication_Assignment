FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/week_5_JavaShoppingCartApplication_Assignment-1.0-SNAPSHOT.jar app.jar




CMD ["java", "-jar", "app.jar"]
