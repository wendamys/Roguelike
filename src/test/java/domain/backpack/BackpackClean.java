package domain.backpack;

import domain.characters.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BackpackTest {

    private Backpack backpack;

    @BeforeEach
    void setUp() {
        backpack = new Backpack();
    }

    @Test
    void testClearListsRemovesAllItems() {
        // 1. Создаем моки для предметов разных типов
        Item elixir = Mockito.mock(Item.class);
        Mockito.when(elixir.getType()).thenReturn(ItemsType.ELIXIR);

        Item food = Mockito.mock(Item.class);
        Mockito.when(food.getType()).thenReturn(ItemsType.FOOD);

        Item scroll = Mockito.mock(Item.class);
        Mockito.when(scroll.getType()).thenReturn(ItemsType.SCROLL);

        Item weapon = Mockito.mock(Item.class);
        Mockito.when(weapon.getType()).thenReturn(ItemsType.WEAPON);

        // 2. Добавляем предметы в рюкзак
        backpack.takeItem(elixir);
        backpack.takeItem(food);
        backpack.takeItem(scroll);
        backpack.takeItem(weapon);

        // 3. Вызываем тестируемый метод очистки
        backpack.clearLists();


        //  методы получения списков скрыты,
        // проверим пустоту через попытку использования предметов.
        // Если список пуст, обращение по индексу 0 вызовет ошибку, перехватим её.

        boolean isFoodListEmpty = false;
        try {
            Player mockPlayer = Mockito.mock(Player.class);
            backpack.useItemFood(0, mockPlayer);
        } catch (IndexOutOfBoundsException e) {
            isFoodListEmpty = true;
        }

        boolean isScrollListEmpty = false;
        try {
            Player mockPlayer = Mockito.mock(Player.class);
            backpack.useItemScroll(0, mockPlayer);
        } catch (IndexOutOfBoundsException e) {
            isScrollListEmpty = true;
        }

        boolean isElixirListEmpty = false;
        try {
            Player mockPlayer = Mockito.mock(Player.class);
            backpack.useItemElixir(0, mockPlayer);
        } catch (IndexOutOfBoundsException e) {
            isElixirListEmpty = true;
        }

        boolean isWeaponListEmpty = false;
        try {
            Player mockPlayer = Mockito.mock(Player.class);
            backpack.useItemWeapon(0, mockPlayer);
        } catch (IndexOutOfBoundsException e) {
            isWeaponListEmpty = true;
        }

        // Подтверждаем, что каждый список выбросил ошибку (значит, они пусты)
        assertTrue(isFoodListEmpty, "Список еды должен быть пуст");
        assertTrue(isScrollListEmpty, "Список свитков должен быть пуст");
        assertTrue(isElixirListEmpty, "Список эликсиров должен быть пуст");
        assertTrue(isWeaponListEmpty, "Список оружия должен быть пуст");
    }
}
