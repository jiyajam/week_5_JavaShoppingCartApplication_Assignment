FROM eclipse-temurin:17-jdk

WORKDIR /app

# The inclusion of libglapi-mesa and libosmesa6 is the ONLY way to fix "No toolkit found"
RUN apt-get update && apt-get install -y \
    wget unzip libgl1 libgtk-3-0 libglu1-mesa libglapi-mesa libosmesa6 \
    && wget https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_linux-x64_bin-sdk.zip \
    && unzip openjfx-21.0.1_linux-x64_bin-sdk.zip \
    && mv javafx-sdk-21.0.1 /opt/javafx \
    && rm openjfx-21.0.1_linux-x64_bin-sdk.zip

COPY target/week_5_JavaShoppingCartApplication_Assignment-1.0-SNAPSHOT.jar app.jar

# Use cart.Launcher and Software Rendering flags
CMD ["java", "-Dprism.order=sw", "-Dglass.platform=gtk", \
     "--module-path", "/opt/javafx/lib", "--add-modules", "javafx.controls,javafx.fxml", \
     "-cp", "app.jar", "cart.Main"]