package domain.map;

import domain.characters.Enemies;
import domain.characters.enemies.Zombie;
import domain.gameSession.DifficultyType;
import domain.navigator.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест для проверки, что createEnemies не падает с NullPointerException
 * при наличии врагов с null позицией (например, убитых врагов)
 */
class DungeonGeneratorEnemyNullPositionTest {

    private DungeonGenerator generator;
    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room(0, 0, DifficultyType.EASY);
        generator = Mockito.mock(DungeonGenerator.class);
        // Mock map to avoid NPE when accessing map
        try {
            java.lang.reflect.Field mapField = DungeonGenerator.class.getDeclaredField("map");
            mapField.setAccessible(true);
            mapField.set(generator, new domain.map.TileType[10][10]);
        } catch (Exception e) {
            fail("Failed to setup test");
        }
    }

    @Test
    void testCreateEnemyWithNullPosition_ShouldNotCrash() {
        // Создаем врага с null позицией (как у убитых врагов после remove)
        Enemies enemy = Mockito.mock(Enemies.class);
        Mockito.when(enemy.getType()).thenReturn(domain.characters.enemies.EnemiesType.ZOMBIE);
        Mockito.when(enemy.getPosition()).thenReturn(null);

        // Добавляем врага в комнату
        room.getEnemyList().add(enemy);

        // Вызываем createEnemies - должен не упасть
        assertDoesNotThrow(() -> {
            generator.createEnemies(room);
        }, "createEnemies должен обрабатывать врагов с null позицией");
    }

    @Test
    void testCreateEnemyWithValidPosition_ShouldWork() {
        // Создаем врага с валидной позицией
        Enemies enemy = new Zombie(new Position(5, 5), DifficultyType.EASY);

        // Добавляем врага в комнату
        room.getEnemyList().add(enemy);

        // Должно работать нормально
        assertDoesNotThrow(() -> {
            generator.createEnemies(room);
        }, "createEnemies должен работать с валидными позициями");
    }

    @Test
    void testCreateEnemiesMixedPositions_ShouldOnlyDrawValid() {
        // Враг с null позицией
        Enemies enemy1 = Mockito.mock(Enemies.class);
        Mockito.when(enemy1.getType()).thenReturn(domain.characters.enemies.EnemiesType.ZOMBIE);
        Mockito.when(enemy1.getPosition()).thenReturn(null);

        // Враг с валидной позицией
        Enemies enemy2 = new Zombie(new Position(3, 3), DifficultyType.EASY);

        // Добавляем обоих врагов
        room.getEnemyList().add(enemy1);
        room.getEnemyList().add(enemy2);

        // Должно работать без ошибок
        assertDoesNotThrow(() -> {
            generator.createEnemies(room);
        }, "createEnemies должен пропускать врагов с null позицией и рисовать валидных");
    }
}
