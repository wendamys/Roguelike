package domain.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LevelTests {

    @BeforeEach
    void setUp() {
        // сбрасываем статический уровень в исходное состояние
        Level.setLevelUp(1);
    }

    @Test
    @DisplayName("Проверка дефолтных коэффициентов на 1 уровне")
    void testDefaultCoefficients() {
        // Формула врагов: (1 * 0.1) + 1 = 1.1
        assertEquals(1.1, Level.getCoefEnemy(), 0.001, "Коэффициент врагов на 1 уровне должен быть 1.1");

        // Формула предметов: (1 * 0.15) + 1 = 1.15
        assertEquals(1.15, Level.getCoefItem(), 0.001, "Коэффициент предметов на 1 уровне должен быть 1.15");
    }

    @ParameterizedTest
    @CsvSource({
            "2, 1.2, 1.3",
            "5, 1.5, 1.75",
            "10, 2.0, 2.5"
    })
    @DisplayName("Проверка изменения коэффициентов при смене уровня")
    void testCoefficientsOnDifferentLevels(int targetLevel, double expectedCoefEnemy, double expectedCoefItem) {
        Level.setLevelUp(targetLevel);

        assertEquals(expectedCoefEnemy, Level.getCoefEnemy(), 0.001, "Неверный коэффициент врагов для уровня " + targetLevel);
        assertEquals(expectedCoefItem, Level.getCoefItem(), 0.001, "Неверный коэффициент предметов для уровня " + targetLevel);
    }

    @Test
    @DisplayName("Проверка форматирования метода toString")
    void testToStringFormatting() {
        Level level = new Level();
        Level.setLevelUp(3);

        String expectedOutput = "\nLevel:\nlevelUp: 3\ncoefEnemy: 1,300\ncoefItem: 1,450";

        // Заменяем возможные различия в разделителях (в зависимости от локали системы)
        String actualOutput = level.toString().replace('.', ',');

        assertEquals(expectedOutput, actualOutput, "Метод toString возвращает некорректно отформатированную строку");
    }
}
