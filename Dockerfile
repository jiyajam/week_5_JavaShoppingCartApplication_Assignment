FROM eclipse-temurin:17-jdk

WORKDIR /app

# Install JavaFX
RUN apt-get update && apt-get install -y wget unzip && \
    wget https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_linux-x64_bin-sdk.zip && \
    unzip openjfx-21.0.1_linux-x64_bin-sdk.zip && \
    mv javafx-sdk-21.0.1 /opt/javafx && \
    rm openjfx-21.0.1_linux-x64_bin-sdk.zip

COPY target/week_5_JavaShoppingCartApplication_Assignment-1.0-SNAPSHOT.jar app.jar

CMD ["java", "--module-path", "/opt/javafx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]
