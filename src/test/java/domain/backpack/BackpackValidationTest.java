package domain.backpack;

import domain.characters.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class BackpackValidationTest {

    private Backpack backpack;
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        backpack = new Backpack();
        mockPlayer = Mockito.mock(Player.class);
    }

    @Test
    void testUseItemFood_NegativeIndex_ShouldReturnFalse() {
        Item food = Mockito.mock(Item.class);
        Mockito.when(food.getType()).thenReturn(ItemsType.FOOD);
        backpack.takeItem(food);

        // Отрицательный индекс должен вернуть false
        boolean result = backpack.useItemFood(-1, mockPlayer);
        assertFalse(result, "Метод должен вернуть false для отрицательного индекса");
    }

    @Test
    void testUseItemScroll_NegativeIndex_ShouldReturnFalse() {
        Item scroll = Mockito.mock(Item.class);
        Mockito.when(scroll.getType()).thenReturn(ItemsType.SCROLL);
        backpack.takeItem(scroll);

        boolean result = backpack.useItemScroll(-1, mockPlayer);
        assertFalse(result, "Метод должен вернуть false для отрицательного индекса");
    }

    @Test
    void testUseItemElixir_NegativeIndex_ShouldReturnFalse() {
        Item elixir = Mockito.mock(Item.class);
        Mockito.when(elixir.getType()).thenReturn(ItemsType.ELIXIR);
        backpack.takeItem(elixir);

        boolean result = backpack.useItemElixir(-1, mockPlayer);
        assertFalse(result, "Метод должен вернуть false для отрицательного индекса");
    }

    @Test
    void testUseItemWeapon_NegativeIndex_ShouldReturnFalse() {
        Item weapon = Mockito.mock(Item.class);
        Mockito.when(weapon.getType()).thenReturn(ItemsType.WEAPON);
        backpack.takeItem(weapon);

        boolean result = backpack.useItemWeapon(-1, mockPlayer);
        assertFalse(result, "Метод должен вернуть false для отрицательного индекса");
    }

    @Test
    void testUseItemFood_IndexOutOfBounds_ShouldReturnFalse() {
        // Пустой список
        boolean result = backpack.useItemFood(0, mockPlayer);
        assertFalse(result, "Метод должен вернуть false для индекса out of bounds");

        // Добавляем один предмет, пробуем индекс 1
        Item food = Mockito.mock(Item.class);
        Mockito.when(food.getType()).thenReturn(ItemsType.FOOD);
        backpack.takeItem(food);

        result = backpack.useItemFood(1, mockPlayer);
        assertFalse(result, "Метод должен вернуть false для индекса out of bounds");
    }

    @Test
    void testUseItemScroll_IndexOutOfBounds_ShouldReturnFalse() {
        boolean result = backpack.useItemScroll(0, mockPlayer);
        assertFalse(result, "Метод должен вернуть false для индекса out of bounds");
    }

    @Test
    void testUseItemElixir_IndexOutOfBounds_ShouldReturnFalse() {
        boolean result = backpack.useItemElixir(0, mockPlayer);
        assertFalse(result, "Метод должен вернуть false для индекса out of bounds");
    }

    @Test
    void testUseItemWeapon_IndexOutOfBounds_ShouldReturnFalse() {
        boolean result = backpack.useItemWeapon(0, mockPlayer);
        assertFalse(result, "Метод должен вернуть false для индекса out of bounds");
    }

    @Test
    void testUseItemByIndex_NegativeIndex_ShouldReturnFalse() {
        Item food = Mockito.mock(Item.class);
        Mockito.when(food.getType()).thenReturn(ItemsType.FOOD);
        backpack.takeItem(food);

        boolean result = backpack.useItemByIndex(-1, ItemsType.FOOD, mockPlayer);
        assertFalse(result, "Метод должен вернуть false для отрицательного индекса");
    }

    @Test
    void testUseItemByIndex_IndexOutOfBounds_ShouldReturnFalse() {
        boolean result = backpack.useItemByIndex(0, ItemsType.FOOD, mockPlayer);
        assertFalse(result, "Метод должен вернуть false для индекса out of bounds");
    }

    @Test
    void testUseItemByIndex_InvalidType_ShouldReturnFalse() {
        Item food = Mockito.mock(Item.class);
        Mockito.when(food.getType()).thenReturn(ItemsType.FOOD);
        backpack.takeItem(food);

        // Пытаемся использовать FOOD как WEAPON
        boolean result = backpack.useItemByIndex(0, ItemsType.WEAPON, mockPlayer);
        assertFalse(result, "Метод должен вернуть false для неверного типа");
    }

    @Test
    void testUseItemByIndex_ValidIndex_ShouldReturnTrue() {
        Item food = Mockito.mock(Item.class);
        Mockito.when(food.getType()).thenReturn(ItemsType.FOOD);
        backpack.takeItem(food);

        boolean result = backpack.useItemByIndex(0, ItemsType.FOOD, mockPlayer);
        assertTrue(result, "Метод должен вернуть true для валидного индекса");
    }

    @Test
    void testGetItem_NegativeIndex_ShouldReturnNull() {
        Item item = backpack.getItem(-1, ItemsType.FOOD);
        assertNull(item, "Метод должен вернуть null для отрицательного индекса");
    }

    @Test
    void testGetItem_IndexOutOfBounds_ShouldReturnNull() {
        Item item = backpack.getItem(0, ItemsType.FOOD);
        assertNull(item, "Метод должен вернуть null для пустого списка");
    }

    @Test
    void testGetItem_ValidIndex_ShouldReturnItem() {
        Item food = Mockito.mock(Item.class);
        Mockito.when(food.getType()).thenReturn(ItemsType.FOOD);
        backpack.takeItem(food);

        Item item = backpack.getItem(0, ItemsType.FOOD);
        assertNotNull(item, "Метод должен вернуть предмет для валидного индекса");
    }
}
