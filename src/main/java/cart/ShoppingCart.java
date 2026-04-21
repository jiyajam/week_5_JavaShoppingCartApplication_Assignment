package cart;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private final List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    public double calculateTotal() {
        return items.stream()
                .mapToDouble(Item::getTotalCost)
                .sum();
    }

    public List<Item> getItems() {
        return items;
    }
}