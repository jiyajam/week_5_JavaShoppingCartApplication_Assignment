package cart;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class MainController {

    @FXML private ComboBox<String> languageBox;
    @FXML private TextField itemCountField;
    @FXML private VBox itemsContainer;
    @FXML private Label totalLabel;

    private Locale currentLocale = new Locale("en", "US");
    private ResourceBundle bundle;

    private final Map<String, Locale> languages = Map.of(
            "English", new Locale("en", "US"),
            "Finnish", new Locale("fi", "FI"),
            "Swedish", new Locale("sv", "SE"),
            "Japanese", new Locale("ja", "JP"),
            "Arabic", new Locale("ar", "AR")
    );

    @FXML
    public void initialize() {
        languageBox.getItems().addAll(languages.keySet());
        languageBox.getSelectionModel().select("English");
        bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
    }

    @FXML
    public void changeLanguage() {
        String selected = languageBox.getValue();
        currentLocale = languages.get(selected);
        bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cart/main-view.fxml"), bundle);

            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) languageBox.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generateItemFields() {
        itemsContainer.getChildren().clear();
        int count = Integer.parseInt(itemCountField.getText());

        for (int i = 1; i <= count; i++) {
            HBox row = new HBox(10);

            TextField price = new TextField();
            price.setPromptText(bundle.getString("enter.price"));

            TextField qty = new TextField();
            qty.setPromptText(bundle.getString("enter.quantity"));

            row.getChildren().addAll(new Label("Item " + i), price, qty);
            itemsContainer.getChildren().add(row);
        }
    }

    @FXML
    public void calculateTotal() {
        ShoppingCart cart = new ShoppingCart();

        for (var node : itemsContainer.getChildren()) {
            HBox row = (HBox) node;
            TextField priceField = (TextField) row.getChildren().get(1);
            TextField qtyField = (TextField) row.getChildren().get(2);

            double price = Double.parseDouble(priceField.getText());
            int qty = Integer.parseInt(qtyField.getText());

            cart.addItem(new Item(price, qty));
        }

        totalLabel.setText(String.valueOf(cart.calculateTotal()));
    }
}

