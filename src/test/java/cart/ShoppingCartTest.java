package cart;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

  @Test
  void testItemTotalCost() {
    Item item = new Item(10.0, 3);
    assertEquals(30.0, item.getTotalCost());
  }

  @Test
  void testCartTotalCost() {
    ShoppingCart cart = new ShoppingCart();
    cart.addItem(new Item(5.0, 2));  // 10
    cart.addItem(new Item(3.0, 4));  // 12
    assertEquals(22.0, cart.calculateTotal());
  }

  // --- NEW TESTS FOR HIGHER COVERAGE ---

  @Test
  void testEmptyCartTotal() {
    ShoppingCart cart = new ShoppingCart();
    // Verifies that sum() on an empty stream returns 0.0
    assertEquals(0.0, cart.calculateTotal(), "Total should be 0.0 for an empty cart");
  }

  @Test
  void testGetItems() {
    ShoppingCart cart = new ShoppingCart();
    Item item = new Item(10.0, 1);
    cart.addItem(item);
    // Verifies the getter logic and list size
    assertEquals(1, cart.getItems().size());
    assertTrue(cart.getItems().contains(item));
  }

  @Test
  void testItemGetters() {
    // If your Item class has getPrice() or getQuantity(), test them specifically
    Item item = new Item(15.5, 2);
    // This ensures the fields in the Item class are 'covered'
    assertEquals(15.5, item.getPrice()); // Assuming this getter exists
    assertEquals(2, item.getQuantity()); // Assuming this getter exists
  }
}