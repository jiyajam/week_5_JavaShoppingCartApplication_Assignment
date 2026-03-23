package cart;



import java.util.*;

public class Main {
    public static void main(String[] args) {
        Locale locale = chooseLanguage();
        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", locale);

        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        System.out.println(messages.getString("enter.items"));
        int itemCount = scanner.nextInt();

        ShoppingCart cart = new ShoppingCart();

        for (int i = 1; i <= itemCount; i++) {
            System.out.println(messages.getString("enter.price") + " " + i + ":");
            double price = scanner.nextDouble();

            System.out.println(messages.getString("enter.quantity") + " " + i + ":");
            int quantity = scanner.nextInt();

            cart.addItem(new Item(price, quantity));
        }

        double total = cart.calculateTotal();
        System.out.println(messages.getString("total.cost") + " " + total);
    }

    private static Locale chooseLanguage() {
        System.out.println("Select language / Valitse kieli / Välj språk / 言語を選択:");
        System.out.println("1. English");
        System.out.println("2. Finnish");
        System.out.println("3. Swedish");
        System.out.println("4. Japanese");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        return switch (choice) {
            case 2 -> new Locale("fi", "FI");
            case 3 -> new Locale("sv", "SE");
            case 4 -> new Locale("ja", "JP");
            default -> new Locale("en", "US");
        };
    }
}

