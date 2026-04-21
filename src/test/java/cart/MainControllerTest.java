package cart;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {

  @Test
  void testControllerLogic() {
    MainController controller = new MainController();

    Map<String, String> testTranslations = new HashMap<>();
    testTranslations.put("total.cost", "Total Sum");

    controller.setTranslations(testTranslations);
    controller.setCurrentLocale(Locale.JAPAN);


    assertDoesNotThrow(controller::updateUI);
  }

  @Test
  void testGenerateFieldsSafety() {
    MainController controller = new MainController();

    assertDoesNotThrow(controller::generateItemFields);
  }
}