# Use JDK 17 as per your compiler target
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Install updated dependencies for JavaFX on Linux
# We use libgl1 instead of the old libgl1-mesa-glx
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    libgl1 \
    libgtk-3-0 \
    libglu1-mesa \
    && wget https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_linux-x64_bin-sdk.zip \
    && unzip openjfx-21.0.1_linux-x64_bin-sdk.zip \
    && mv javafx-sdk-21.0.1 /opt/javafx \
    && rm openjfx-21.0.1_linux-x64_bin-sdk.zip

# Use the shaded JAR produced by your Maven build
# Note: Ensure this filename matches your target folder exactly
COPY target/week_5_JavaShoppingCartApplication_Assignment-1.0-SNAPSHOT.jar app.jar

# Run with JavaFX modules linked
CMD ["java", "--module-path", "/opt/javafx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]