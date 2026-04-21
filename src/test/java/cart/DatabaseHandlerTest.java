package cart;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseHandlerTest {

  @Test
  void testGetConnection() throws SQLException {
    // This covers the getConnection() method
    try (Connection conn = DatabaseHandler.getConnection()) {
      assertNotNull(conn, "Connection should not be null");
    }
  }

  @Test
  void testConstructorIsPrivate() throws NoSuchMethodException {
    // This covers the private constructor using reflection
    // This is the "secret" to getting 100% on utility classes
    Constructor<DatabaseHandler> constructor = DatabaseHandler.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    assertThrows(InvocationTargetException.class, constructor::newInstance);
  }
}