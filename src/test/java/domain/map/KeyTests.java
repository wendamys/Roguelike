package domain.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class KeyTests {

    @BeforeEach
    @SuppressWarnings("unchecked")
    void resetStaticState() {
        try {
            // Безопасно очищаем статический список choiceColorList перед каждым тестом.
            // Это гарантирует, что тесты всегда будут стартовать с чистого листа (с цвета GREEN).
            Field field = Key.class.getDeclaredField("choiceColorList");
            field.setAccessible(true);
            ArrayList<ColorKey> choiceColorList = (ArrayList<ColorKey>) field.get(null);
            choiceColorList.clear();
        } catch (Exception e) {
            fail("Не удалось сбросить статический кэш класса Key: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка последовательности цветов ключей и превышения лимита")
    void testKeySequenceAndLimit() {
        // Теперь первый ключ гарантированно будет GREEN
        Key firstKey = new Key();
        assertEquals(ColorKey.GREEN, firstKey.getColorKey(), "Первый ключ должен быть GREEN");

        Key secondKey = new Key();
        assertEquals(ColorKey.BLUE, secondKey.getColorKey(), "Второй ключ должен быть BLUE");

        Key thirdKey = new Key();
        assertEquals(ColorKey.RED, thirdKey.getColorKey(), "Третий ключ должен быть RED");

        Key fourthKey = new Key();
        assertEquals(ColorKey.YELLOW, fourthKey.getColorKey(), "Четвертый ключ должен быть YELLOW");

        Key fifthKey = new Key();
        assertNull(fifthKey.getColorKey(), "Пятый ключ должен возвращать null, так как цвета закончились");
    }

    @Test
    @DisplayName("Метод removeColorKeyList не должен выбрасывать ошибок при вызове")
    void testRemoveColorKeyListDoesNotThrow() {
        Key key = new Key();
        assertDoesNotThrow(() -> key.removeColorKeyList(ColorKey.GREEN));
    }
}
