//package cart;
//
//
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
////
//import java.io.IOException;
//import java.util.*;
//
//public class MainController {
//
//    @FXML private ComboBox<String> languageBox;
//    @FXML private TextField itemCountField;
//    @FXML private VBox itemsContainer;
//    @FXML private Label totalLabel;
//
//    private Locale currentLocale = new Locale("en", "US");
//    private ResourceBundle bundle;

//    private final Map<String, Locale> languages = Map.of(
//            "English", new Locale("en", "US"),
//            "Finnish", new Locale("fi", "FI"),
//            "Swedish", new Locale("sv", "SE"),
//            "Japanese", new Locale("ja", "JP"),
//            "Arabic", new Locale("ar", "AR")
//    );
//
//    @FXML
//    public void initialize() {
//        languageBox.getItems().addAll(languages.keySet());
//        languageBox.getSelectionModel().select("English");
//        bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
//    }
//
//    @FXML
//    public void changeLanguage() {
//        String selected = languageBox.getValue();
//        currentLocale = languages.get(selected);
//        bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cart/main-view.fxml"), bundle);
//
//            Scene scene = new Scene(loader.load());
//            Stage stage = (Stage) languageBox.getScene().getWindow();
//            stage.setScene(scene);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    public void generateItemFields() {
//        itemsContainer.getChildren().clear();
//        int count = Integer.parseInt(itemCountField.getText());
//
//        for (int i = 1; i <= count; i++) {
//            HBox row = new HBox(10);
//
//            TextField price = new TextField();
//            price.setPromptText(bundle.getString("enter.price"));
//
//            TextField qty = new TextField();
//            qty.setPromptText(bundle.getString("enter.quantity"));
//
//            row.getChildren().addAll(new Label("Item " + i), price, qty);
//            itemsContainer.getChildren().add(row);
//        }
//    }
//
//    @FXML
//    public void calculateTotal() {
//        ShoppingCart cart = new ShoppingCart();
//
//        for (var node : itemsContainer.getChildren()) {
//            HBox row = (HBox) node;
//            TextField priceField = (TextField) row.getChildren().get(1);
//            TextField qtyField = (TextField) row.getChildren().get(2);
//
//            double price = Double.parseDouble(priceField.getText());
//            int qty = Integer.parseInt(qtyField.getText());
//
//            cart.addItem(new Item(price, qty));
//        }
//
//        totalLabel.setText(String.valueOf(cart.calculateTotal()));
//    }
//}
//
package cart;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.NodeOrientation;

import java.io.IOException;
import java.util.*;

public class MainController {

    // FXML UI Components (Must match fx:id in main-view.fxml)
    @FXML private ComboBox<String> languageBox;
    @FXML private TextField itemCountField;
    @FXML private VBox itemsContainer;
    @FXML private Label totalLabel;

    @FXML private Label selectLangLabel;
    @FXML private Button confirmBtn;
    @FXML private Label enterItemsLabel;
    @FXML private Button generateBtn;
    @FXML private Button calculateBtn;

    private Map<String, String> translations = new HashMap<>();
    private Locale currentLocale = new Locale("en", "US");
    private final CartService cartService = new CartService();

    private final Map<String, Locale> languages = Map.of(
            "English", new Locale("en", "US"),
            "Finnish", new Locale("fi", "FI"),
            "Swedish", new Locale("sv", "SE"),
            "Japanese", new Locale("ja", "JP"),
            "Arabic", new Locale("ar", "SA")
    );

    @FXML
    public void initialize() {
        // Only populate if the list is empty (prevents duplicates on reload)
        if (languageBox.getItems().isEmpty()) {
            languageBox.getItems().addAll(languages.keySet());
            languageBox.getSelectionModel().select("English");
        }
        loadTranslationsFromDatabase();
    }

    // Setters used by changeLanguage to pass data to the new Controller instance
    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }

    public void setCurrentLocale(Locale locale) {
        this.currentLocale = locale;
    }

    private void loadTranslationsFromDatabase() {
        this.translations = cartService.getTranslations(currentLocale.getLanguage());
        updateUI();
    }

    public void updateUI() {
        if (translations == null || translations.isEmpty()) return;

        // Update labels and buttons from the Database Map
        if (selectLangLabel != null) selectLangLabel.setText(translations.getOrDefault("select.language", "Select Language"));
        if (confirmBtn != null) confirmBtn.setText(translations.getOrDefault("confirm.language", "Confirm"));
        if (enterItemsLabel != null) enterItemsLabel.setText(translations.getOrDefault("enter.items", "Number of items"));
        if (generateBtn != null) generateBtn.setText(translations.getOrDefault("enter.items.button", "Generate"));
        if (calculateBtn != null) calculateBtn.setText(translations.getOrDefault("calculate.total", "Calculate"));
    }

    @FXML
    public void changeLanguage() {
        String selected = languageBox.getValue();
        if (selected == null) return;

        currentLocale = languages.get(selected);
        loadTranslationsFromDatabase();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cart/main-view.fxml"));
            Parent root = loader.load();

            // Hand off the data to the new controller instance created by the loader
            MainController newController = loader.getController();
            newController.setTranslations(this.translations);
            newController.setCurrentLocale(this.currentLocale);
            newController.updateUI();

            Scene scene = new Scene(root);
            Stage stage = (Stage) languageBox.getScene().getWindow();

            // The Arabic "Flip" (RTL)
            if (currentLocale.getLanguage().equals("ar")) {
                scene.getRoot().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            } else {
                scene.getRoot().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            }

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generateItemFields() {
        itemsContainer.getChildren().clear();
        try {
            int count = Integer.parseInt(itemCountField.getText());
            for (int i = 1; i <= count; i++) {
                HBox row = new HBox(10);
                TextField price = new TextField();
                price.setPromptText(translations.getOrDefault("enter.price", "Price"));
                TextField qty = new TextField();
                qty.setPromptText(translations.getOrDefault("enter.quantity", "Qty"));

                row.getChildren().addAll(new Label("Item " + i), price, qty);
                itemsContainer.getChildren().add(row);
            }
        } catch (NumberFormatException e) {
            totalLabel.setText("Invalid count");
        }
    }

    @FXML
    public void calculateTotal() {
        ShoppingCart cart = new ShoppingCart();
        try {
            for (var node : itemsContainer.getChildren()) {
                HBox row = (HBox) node;
                TextField priceField = (TextField) row.getChildren().get(1);
                TextField qtyField = (TextField) row.getChildren().get(2);

                double price = Double.parseDouble(priceField.getText());
                int qty = Integer.parseInt(qtyField.getText());

                cart.addItem(new Item(price, qty));
            }

            double total = cart.calculateTotal();
            String totalText = translations.getOrDefault("total.cost", "Total");
            totalLabel.setText(totalText + ": " + total);

            // Save to MariaDB
            cartService.saveCart(cart, currentLocale.getLanguage());

        } catch (Exception e) {
            totalLabel.setText("Error in input fields");
        }
    }
}