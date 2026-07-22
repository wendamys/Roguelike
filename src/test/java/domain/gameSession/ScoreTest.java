package domain.gameSession;

import domain.map.Level;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreTest {

    @Test
    @DisplayName("Счёт учитывает золото, убитых врагов, уровень и коэффициент сложности")
    void scoreFormula() {
        Game game = new Game(DifficultyType.HARD);
        game.getPlayer().setGold(100);
        game.setEnemiesKilled(3);
        Level.setLevelUp(2);

        // (100 + 3*10 + (2-1)*50) * 1.15 = 180 * 1.15 = 207
        assertEquals(207, game.calculateScore());
    }

    @Test
    @DisplayName("На старте счёт равен нулю")
    void scoreStartsAtZero() {
        Game game = new Game(DifficultyType.EASY);
        Level.setLevelUp(1);
        game.getPlayer().setGold(0);
        game.setEnemiesKilled(0);

        assertEquals(0, game.calculateScore());
    }
}
