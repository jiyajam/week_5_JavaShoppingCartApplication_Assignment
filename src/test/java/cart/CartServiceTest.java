package cart;

import org.junit.jupiter.api.*;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CartServiceTest {

  private CartService cartService;

  @BeforeEach
  void setUp() {
    cartService = new CartService();
  }

  @Test
  @Order(1)
  void testGetTranslationsSuccess() {

    Map<String, String> translations = cartService.getTranslations("en");
    assertNotNull(translations);

  }

  @Test
  @Order(2)
  void testSaveCartSuccess() {
    ShoppingCart cart = new ShoppingCart();
    cart.addItem(new Item(10.0, 2));
    cart.addItem(new Item(5.0, 1));

    // This covers the transaction start, batch execution, and commit logic
    assertDoesNotThrow(() -> cartService.saveCart(cart, "en"));
  }

  @Test
  @Order(3)
  void testGetTranslationsWithNonExistentLang() {
    // Covers the path where the ResultSet is empty
    Map<String, String> translations = cartService.getTranslations("non_existent");
    assertTrue(translations.isEmpty());
  }

  @Test
  @Order(4)
  void testSaveEmptyCart() {
    ShoppingCart cart = new ShoppingCart();
    // Covers the path where getGeneratedKeys runs but the item loop is skipped
    assertDoesNotThrow(() -> cartService.saveCart(cart, "fi"));
  }
  @Test
  @Order(5)
  void testGetTranslationsDatabaseError() {

    assertDoesNotThrow(() -> cartService.getTranslations(null));
  }



}