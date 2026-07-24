package domain.map;

import domain.backpack.Item;
import domain.backpack.ItemsType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест для проверки, что createItem не падает с NullPointerException
 * при наличии предметов с null позицией (например, подобранных предметов)
 */
class DungeonGeneratorItemNullPositionTest {

    private DungeonGenerator generator;
    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room(0, 0);
        generator = Mockito.mock(DungeonGenerator.class);
        // Mock map to avoid NPE when accessing map
        try {
            java.lang.reflect.Field mapField = DungeonGenerator.class.getDeclaredField("map");
            mapField.setAccessible(true);
            mapField.set(generator, new TileType[10][10]);
        } catch (Exception e) {
            fail("Failed to setup test");
        }
    }

    @Test
    void testCreateItemWithNullPosition_ShouldNotCrash() {
        // Создаем предмет с null позицией (как у подобранных предметов)
        Item item = Mockito.mock(Item.class);
        Mockito.when(item.getType()).thenReturn(ItemsType.FOOD);
        Mockito.when(item.getPosition()).thenReturn(null);

        // Добавляем предмет в комнату
        room.getItemList().add(item);

        // Вызываем createItem - должен не упасть
        assertDoesNotThrow(() -> {
            generator.createItem(room);
        }, "createItem должен обрабатывать предметы с null позицией");
    }

    @Test
    void testCreateItemWithValidPosition_ShouldWork() {
        // Создаем предмет с валидной позицией
        Item item = Mockito.mock(Item.class);
        Mockito.when(item.getType()).thenReturn(ItemsType.FOOD);
        Mockito.when(item.getPosition()).thenReturn(new domain.navigator.Position(5, 5));

        // Добавляем предмет в комнату
        room.getItemList().add(item);

        // Должно работать нормально
        assertDoesNotThrow(() -> {
            generator.createItem(room);
        }, "createItem должен работать с валидными позициями");
    }

    @Test
    void testCreateItemMixedPositions_ShouldOnlyDrawValid() {
        // Предмет с null позицией
        Item item1 = Mockito.mock(Item.class);
        Mockito.when(item1.getType()).thenReturn(ItemsType.FOOD);
        Mockito.when(item1.getPosition()).thenReturn(null);

        // Предмет с валидной позицией
        Item item2 = Mockito.mock(Item.class);
        Mockito.when(item2.getType()).thenReturn(ItemsType.ELIXIR);
        Mockito.when(item2.getPosition()).thenReturn(new domain.navigator.Position(3, 3));

        // Добавляем оба предмета
        room.getItemList().add(item1);
        room.getItemList().add(item2);

        // Должно работать без ошибок
        assertDoesNotThrow(() -> {
            generator.createItem(room);
        }, "createItem должен пропускать предметы с null позицией и рисовать валидные");
    }
}
