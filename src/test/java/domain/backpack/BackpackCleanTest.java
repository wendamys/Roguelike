package domain.backpack;

import domain.characters.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BackpackTest {

    private Backpack backpack;

    @BeforeEach
    void setUp() {
        backpack = new Backpack();
    }

    @Test
    void testClearListsRemovesAllItems() {
        // Создаем моки для предметов разных типов
        Item elixir = Mockito.mock(Item.class);
        Mockito.when(elixir.getType()).thenReturn(ItemsType.ELIXIR);

        Item food = Mockito.mock(Item.class);
        Mockito.when(food.getType()).thenReturn(ItemsType.FOOD);

        Item scroll = Mockito.mock(Item.class);
        Mockito.when(scroll.getType()).thenReturn(ItemsType.SCROLL);

        Item weapon = Mockito.mock(Item.class);
        Mockito.when(weapon.getType()).thenReturn(ItemsType.WEAPON);

        // Добавляем предметы в рюкзак
        backpack.takeItem(elixir);
        backpack.takeItem(food);
        backpack.takeItem(scroll);
        backpack.takeItem(weapon);

        // Проверяем, что списки не пусты (использование предмета возвращает true)
        assertTrue(backpack.useItemFood(0, Mockito.mock(Player.class)), "Еда должна быть использована");
        assertTrue(backpack.useItemScroll(0, Mockito.mock(Player.class)), "Свиток должен быть использован");
        assertTrue(backpack.useItemElixir(0, Mockito.mock(Player.class)), "Эликсир должен быть использован");
        assertTrue(backpack.useItemWeapon(0, Mockito.mock(Player.class)), "Оружие должно быть использовано");

        // Вызываем тестируемый метод очистки
        backpack.clearLists();

        // Проверяем, что списки пусты (использование предмета возвращает false)
        boolean isFoodListEmpty = !backpack.useItemFood(0, Mockito.mock(Player.class));
        boolean isScrollListEmpty = !backpack.useItemScroll(0, Mockito.mock(Player.class));
        boolean isElixirListEmpty = !backpack.useItemElixir(0, Mockito.mock(Player.class));
        boolean isWeaponListEmpty = !backpack.useItemWeapon(0, Mockito.mock(Player.class));

        assertTrue(isFoodListEmpty, "Список еды должен быть пуст");
        assertTrue(isScrollListEmpty, "Список свитков должен быть пуст");
        assertTrue(isElixirListEmpty, "Список эликсиров должен быть пуст");
        assertTrue(isWeaponListEmpty, "Список оружия должен быть пуст");
    }
}
