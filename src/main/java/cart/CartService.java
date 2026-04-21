package cart;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartService {

  private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());
  private static final String URL = "jdbc:mariadb://localhost:3306/shopping_cart_localization";
  private static final String USER = "root";

  private static final Dotenv DOTENV = Dotenv.load();
  private static final String PASS = DOTENV.get("DB_PASSWORD");

  public Map<String, String> getTranslations(String langCode) {
    Map<String, String> translations = new HashMap<>();
    String query = "SELECT message_key, message_text FROM translations WHERE language_code = ?";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
         PreparedStatement stmt = conn.prepareStatement(query)) {

      stmt.setString(1, langCode);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          translations.put(rs.getString("message_key"), rs.getString("message_text"));
        }
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Database error fetching translations", e);
    }
    return translations;
  }

  public void saveCart(ShoppingCart cart, String language) {
    String recordSql = "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)";
    String itemSql = "INSERT INTO cart_items (cart_record_id, item_number, price, quantity) VALUES (?, ?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
      conn.setAutoCommit(false);

      int cartRecordId = insertCartRecord(conn, recordSql, cart, language);

      if (cartRecordId != -1) {
        insertCartItems(conn, itemSql, cart, cartRecordId);
        conn.commit();
        LOGGER.info("Cart saved successfully.");
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Database transaction failed", e);
    }
  }

  private int insertCartRecord(Connection conn, String sql, ShoppingCart cart, String lang) throws SQLException {
    try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setInt(1, cart.getItems().size());
      ps.setDouble(2, cart.calculateTotal());
      ps.setString(3, lang);
      ps.executeUpdate();

      try (ResultSet rs = ps.getGeneratedKeys()) {
        return rs.next() ? rs.getInt(1) : -1;
      }
    }
  }

  private void insertCartItems(Connection conn, String sql, ShoppingCart cart, int recordId) throws SQLException {
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      int itemNum = 1;

      // Fix: Set the constant parameter ONCE outside the loop
      ps.setInt(1, recordId);

      for (Item item : cart.getItems()) {
        // Only set the parameters that change
        ps.setInt(2, itemNum++);
        ps.setDouble(3, item.getPrice());
        ps.setInt(4, item.getQuantity());

        ps.addBatch();
        // Note: We do NOT call clearParameters() here because we want
        // the recordId (param 1) to persist for the next batch entry.
      }
      ps.executeBatch();
    }

  }
}