package domain.backpack;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BackpackClean  {
    private Backpack backpack;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        backpack = new Backpack();
        // Перехватываем стандартный вывод консоли System.out
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        // Возвращаем стандартную консоль обратно после каждого теста
        System.setOut(originalOut);
    }

    @Test
    void testTakeItem_Elixir_ShouldAddWithoutErrors() {
        Item elixir = mock(Item.class);
        when(elixir.getType()).thenReturn(ItemsType.ELIXIR);
        when(elixir.toString()).thenReturn("MockElixir");

        // Вызываем метод добавления
        assertDoesNotThrow(() -> backpack.takeItem(elixir));

        // Вызываем вывод списка в наш перехваченный поток
        backpack.seeList(ItemsType.ELIXIR);

        // Проверяем, что в консоль вывелся наш эликсир (значит он добавился в список)
        assertTrue(outputStreamCaptor.toString().trim().contains("MockElixir"),
                "Эликсир должен быть добавлен в рюкзак");
    }

    @Test
    void testTakeItem_Food_ShouldAddWithoutErrors() {
        Item food = mock(Item.class);
        when(food.getType()).thenReturn(ItemsType.FOOD);
        when(food.toString()).thenReturn("MockFood");

        assertDoesNotThrow(() -> backpack.takeItem(food));

        backpack.seeList(ItemsType.FOOD);
        assertTrue(outputStreamCaptor.toString().trim().contains("MockFood"),
                "Еда должна быть добавлена в рюкзак");
    }

    @Test
    void testTakeItem_Scroll_ShouldAddWithoutErrors() {
        Item scroll = mock(Item.class);
        when(scroll.getType()).thenReturn(ItemsType.SCROLL);
        when(scroll.toString()).thenReturn("MockScroll");

        assertDoesNotThrow(() -> backpack.takeItem(scroll));

        backpack.seeList(ItemsType.SCROLL);
        assertTrue(outputStreamCaptor.toString().trim().contains("MockScroll"),
                "Свиток должен быть добавлен в рюкзак");
    }

    @Test
    void testTakeItem_Weapon_ShouldAddWhenSpaceAvailable() {
        Item weapon = mock(Item.class);
        when(weapon.getType()).thenReturn(ItemsType.WEAPON);
        when(weapon.toString()).thenReturn("MockWeapon");

        assertDoesNotThrow(() -> backpack.takeItem(weapon));

        backpack.seeList(ItemsType.WEAPON);
        assertTrue(outputStreamCaptor.toString().trim().contains("MockWeapon"),
                "Оружие должно быть добавлено в рюкзак");
    }
}
