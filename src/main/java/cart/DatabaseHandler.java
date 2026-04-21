package cart;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {

  private static final String URL = "jdbc:mariadb://localhost:3306/shopping_cart_localization";
  private static final String USER = "root";

  private static final Dotenv DOTENV = Dotenv.load();
  private static final String PASS = DOTENV.get("DB_PASSWORD");

  private DatabaseHandler() {
    throw new IllegalStateException("Utility class");
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASS);
  }
}