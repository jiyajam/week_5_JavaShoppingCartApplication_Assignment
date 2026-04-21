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
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {
  // 1. Logger for SonarQube "A" Rating
  private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

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
    if (languageBox != null && languageBox.getItems().isEmpty()) {
      languageBox.getItems().addAll(languages.keySet());
      languageBox.getSelectionModel().select("English");
    }
    loadTranslationsFromDatabase();
  }

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

      MainController newController = loader.getController();
      newController.setTranslations(this.translations);
      newController.setCurrentLocale(this.currentLocale);
      newController.updateUI();

      Scene scene = new Scene(root);
      Stage stage = (Stage) languageBox.getScene().getWindow();

      scene.getRoot().setNodeOrientation(currentLocale.getLanguage().equals("ar")
              ? NodeOrientation.RIGHT_TO_LEFT
              : NodeOrientation.LEFT_TO_RIGHT);

      stage.setScene(scene);
    } catch (IOException e) {
      // 2. Fixed e.printStackTrace()
      LOGGER.log(Level.SEVERE, "Failed to load fxml", e);
    }
  }

  @FXML
  public void generateItemFields() {
    if (itemsContainer == null) return;
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
    } catch (NumberFormatException | NullPointerException e) {
      if (totalLabel != null) totalLabel.setText("Invalid count");
    }
  }

  @FXML
  public void calculateTotal() {
    ShoppingCart cart = new ShoppingCart();
    try {
      if (itemsContainer == null) return;
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
      if (totalLabel != null) totalLabel.setText(totalText + ": " + total);

      cartService.saveCart(cart, currentLocale.getLanguage());

    } catch (Exception e) {
      if (totalLabel != null) totalLabel.setText("Error in input fields");
    }
  }
}