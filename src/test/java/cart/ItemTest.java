package cart;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

  @Test
  void testItemCalculations() {
    Item item = new Item(19.99, 3);

    // Tests getTotalCost() logic
    assertEquals(59.97, item.getTotalCost(), 0.001);

    // Tests Getters (important for coverage percentage!)
    assertEquals(19.99, item.getPrice());
    assertEquals(3, item.getQuantity());
  }

  @Test
  void testZeroQuantity() {
    Item item = new Item(10.0, 0);
    assertEquals(0.0, item.getTotalCost());
  }


}