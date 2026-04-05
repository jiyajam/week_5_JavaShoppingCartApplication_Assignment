package cart;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CartService {
    private final String url = "jdbc:mariadb://localhost:3306/shopping_cart_localization";
    private final String user = "root";
    private final String pass = "password";

    // TASK: Read UI messages from a database table
    public Map<String, String> getTranslations(String langCode) {
        Map<String, String> translations = new HashMap<>();
        String query = "SELECT message_key, message_text FROM translations WHERE language_code = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, langCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                translations.put(rs.getString("message_key"), rs.getString("message_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return translations;
    }

    // TASK: Save shopping cart calculations and individual items
    public void saveCart(ShoppingCart cart, String language) {
        String recordSql = "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)";
        String itemSql = "INSERT INTO cart_items (cart_record_id, item_number, price, quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            conn.setAutoCommit(false); // Start Transaction

            // 1. Save main record and get the ID
            try (PreparedStatement psRecord = conn.prepareStatement(recordSql, Statement.RETURN_GENERATED_KEYS)) {
                psRecord.setInt(1, cart.getItems().size());
                psRecord.setDouble(2, cart.calculateTotal());
                psRecord.setString(3, language);
                psRecord.executeUpdate();

                ResultSet rs = psRecord.getGeneratedKeys();
                if (rs.next()) {
                    int cartRecordId = rs.getInt(1);

                    // 2. Save individual items using the Foreign Key
                    try (PreparedStatement psItem = conn.prepareStatement(itemSql)) {
                        int itemNum = 1;
                        for (Item item : cart.getItems()) {
                            psItem.setInt(1, cartRecordId);
                            psItem.setInt(2, itemNum++);
                            psItem.setDouble(3, item.getPrice());
                            psItem.setInt(4, item.getQuantity());
                            psItem.addBatch();
                        }
                        psItem.executeBatch();
                    }
                }
                conn.commit(); // Success!
                System.out.println("Cart saved successfully to MariaDB.");
            } catch (SQLException e) {
                conn.rollback(); // Undo if something goes wrong
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}