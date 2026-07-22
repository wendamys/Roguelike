package domain.gameSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DifficultyTypeTest {

    @Test
    @DisplayName("Коэффициенты сложности соответствуют ТЗ")
    void coefValues() {
        assertEquals(1.0, DifficultyType.EASY.getCoef());
        assertEquals(1.15, DifficultyType.HARD.getCoef());
        assertEquals(1.3, DifficultyType.VERY_HARD.getCoef());
    }

    @Test
    @DisplayName("Диапазон ассортимента магазина сужается с ростом сложности")
    void shopRange() {
        assertEquals(2, DifficultyType.EASY.getShopMin());
        assertEquals(4, DifficultyType.EASY.getShopMax());
        assertEquals(1, DifficultyType.HARD.getShopMin());
        assertEquals(3, DifficultyType.HARD.getShopMax());
        assertEquals(0, DifficultyType.VERY_HARD.getShopMin());
        assertEquals(2, DifficultyType.VERY_HARD.getShopMax());
    }

    @Test
    @DisplayName("Новая игра по умолчанию на EASY")
    void gameDefaultsToEasy() {
        assertEquals(DifficultyType.EASY, new Game().getDifficulty());
    }

    @Test
    @DisplayName("Сложность передаётся в игру через конструктор")
    void gameKeepsDifficulty() {
        assertEquals(DifficultyType.VERY_HARD, new Game(DifficultyType.VERY_HARD).getDifficulty());
    }

    @Test
    @DisplayName("У каждой сложности есть подпись для UI")
    void labelsPresent() {
        for (DifficultyType type : DifficultyType.values()) {
            assertTrue(type.getLabel() != null && !type.getLabel().isBlank());
        }
    }
}
