FROM eclipse-temurin:17-jdk

WORKDIR /app

# The addition of libglapi-mesa and libosmesa6 fixes the "No toolkit found" error
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    libgl1 \
    libgtk-3-0 \
    libglu1-mesa \
    libglapi-mesa \
    libosmesa6 \
    && wget https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_linux-x64_bin-sdk.zip \
    && unzip openjfx-21.0.1_linux-x64_bin-sdk.zip \
    && mv javafx-sdk-21.0.1 /opt/javafx \
    && rm openjfx-21.0.1_linux-x64_bin-sdk.zip

COPY target/week_5_JavaShoppingCartApplication_Assignment-1.0-SNAPSHOT.jar app.jar

# We point to cart.Launcher as the main class
CMD ["java", "-Dprism.order=sw", "-Dglass.platform=gtk", "--module-path", "/opt/javafx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-cp", "app.jar", "cart.Launcher"]