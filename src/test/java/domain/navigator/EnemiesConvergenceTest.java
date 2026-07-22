package domain.navigator;

import domain.characters.Player;
import domain.characters.enemies.Zombie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnemiesConvergenceTest {

    private static final Predicate<Position> ALL_WALKABLE = pos -> true;

    @Test
    @DisplayName("Без препятствий враг идёт прямо на игрока")
    void convergenceMovesTowardPlayer() {
        MovementSystem mv = new MovementSystem();

        Zombie zombie = new Zombie(new Position(0, 0));
        Player player = new Player(new Position(0, 1));

        DirectionType dt = zombie.convergence(player, ALL_WALKABLE);
        mv.moveDir(dt, zombie);

        assertEquals(1, zombie.getPosition().getY());
    }

    @Test
    @DisplayName("Враг не выбирает направление в стену, а обходит её")
    void convergenceAvoidsWalls() {
        Zombie zombie = new Zombie(new Position(5, 5));
        Player player = new Player(new Position(5, 9));

        // Прямой путь вниз перекрыт, свободны только LEFT и RIGHT
        Set<Position> blocked = Set.of(new Position(5, 6), new Position(5, 4));
        Predicate<Position> walkable = pos -> blocked.stream().noneMatch(pos::equals);

        DirectionType dt = zombie.convergence(player, walkable);

        assertNotNull(dt, "Есть проходимые клетки - направление должно быть найдено");
        assertTrue(dt == DirectionType.LEFT || dt == DirectionType.RIGHT,
                "Ожидался обход, а не шаг в стену, получено: " + dt);
    }

    @Test
    @DisplayName("Если все соседние клетки заняты, враг остаётся на месте")
    void convergenceReturnsNullWhenTrapped() {
        Zombie zombie = new Zombie(new Position(3, 3));
        Player player = new Player(new Position(9, 9));

        assertNull(zombie.convergence(player, pos -> false));
    }
}
