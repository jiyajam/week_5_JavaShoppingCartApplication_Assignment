package cart;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTest {

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
}

