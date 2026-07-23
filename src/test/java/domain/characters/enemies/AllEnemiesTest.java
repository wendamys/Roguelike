package domain.characters.enemies;
import domain.characters.Enemies;


import domain.map.Level;
import domain.navigator.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AllEnemiesTest {

    private MockedStatic<Level> mockedLevel;

    @BeforeEach
    void setUp() {
        // Замораживаем коэффициент уровня, чтобы проверить базовые значения
        mockedLevel = mockStatic(Level.class);
        mockedLevel.when(Level::getCoefEnemy).thenReturn(1.0);
    }

    @AfterEach
    void tearDown() {
        mockedLevel.close();
    }

    static Stream<Enemies> enemyProvider() {
        Position mockPos = mock(Position.class);
        return Stream.of(
                new Ghost(mockPos, null),
                new Mimic(mockPos, null),
                new Ogre(mockPos, null),
                new Snake(mockPos, null),
                new Vampire(mockPos, null),
                new Zombie(mockPos, null)
        );
    }

    @ParameterizedTest
    @MethodSource("enemyProvider")
    void testMonsterStats_ShouldBeValidAndPresent(Enemies enemy) {
        String monsterName = enemy.getClass().getSimpleName();

        // Проверяем имя на карте
        assertNotNull(enemy.getName(), "Имя не задано у " + monsterName);
        assertFalse(enemy.getName().trim().isEmpty(), "Имя пустое у " + monsterName);

        // Проверяем тип из EnemiesType
        assertNotNull(enemy.getType(), "Тип EnemiesType равен null у " + monsterName);

        // Проверяем баланс: статы должны быть строго положительными
        assertTrue(enemy.getHealth() > 0, "Здоровье <= 0 у " + monsterName);
        assertTrue(enemy.getStrength() > 0, "Сила <= 0 у " + monsterName);
        assertTrue(enemy.getAgility() > 0, "Ловкость <= 0 у " + monsterName);

        // Проверяем враждебность
        assertTrue(enemy.getHostility() > 0, "Уровень агрессии должен быть выше 0 у " + monsterName);
    }

    @ParameterizedTest
    @MethodSource("enemyProvider")
    void testToString_ShouldContainEssentialInfo(Enemies enemy) {
        String info = enemy.toString();
        String monsterName = enemy.getClass().getSimpleName();

        // Гарантируем, что переопределенный toString выводит ключевые статы для логов боя
        assertTrue(info.contains("health:"), "toString() не выводит здоровье у " + monsterName);
        assertTrue(info.contains("strength:"), "toString() не выводит силу у " + monsterName);
        assertTrue(info.contains("agility:"), "toString() не выводит ловкость у " + monsterName);
    }
}
