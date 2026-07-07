package domain.backpack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BackpackConsumablesCapacityTest {
    private Backpack backpack;

    @BeforeEach
    void setUp() {
        backpack = new Backpack();
    }

    @Test
    void testFoodList_ShouldNotCrashOnOverflow() {
        Item food = mock(Item.class);
        when(food.getType()).thenReturn(ItemsType.FOOD);

        // пытаемся добавить 10 элементов (лимит 9)
        // Метод addIfPossible проигнорирует 10-й элемент
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                backpack.takeItem(food);
            }
        });
    }

    @Test
    void testElixirList_ShouldNotCrashOnOverflow() {
        Item elixir = mock(Item.class);
        when(elixir.getType()).thenReturn(ItemsType.ELIXIR);

        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                backpack.takeItem(elixir);
            }
        });
    }
}
