package domain.battle;

import domain.characters.Player;
import domain.characters.enemies.Zombie;
import domain.gameSession.DifficultyType;
import domain.map.DungeonGenerator;
import domain.navigator.DirectionType;
import domain.navigator.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Боёвка должна быть честной: враг бьёт ровно оттуда, откуда игрок может ответить.
 * Игрок ходит и атакует по WASD, то есть только ортогонально.
 */
public class MeleeSymmetryTest {

    private static final Predicate<Position> ALL_WALKABLE = pos -> true;

    @Test
    @DisplayName("Враг вплотную к игроку не отходит в сторону")
    void adjacentEnemyStandsStill() {
        Player player = new Player(new Position(10, 10));
        Zombie zombie = new Zombie(new Position(10, 11), );

        // клетка игрока занята, остальное свободно - как в реальном ходу
        Predicate<Position> walkable = pos -> !pos.equals(player.getPosition());

        assertNull(zombie.decideMove(player, walkable),
                "Враг вплотную должен стоять и бить, а не уходить на диагональ");
    }

    @Test
    @DisplayName("Враг с диагонали подходит ортогонально, а не топчется")
    void diagonalEnemyStepsToOrthogonal() {
        Player player = new Player(new Position(10, 10));
        Zombie zombie = new Zombie(new Position(11, 11), );

        Predicate<Position> walkable = pos -> !pos.equals(player.getPosition());
        DirectionType dir = zombie.decideMove(player, walkable);

        Position next = dir.applyTo(zombie.getPosition());
        assertTrue(next.distanceTo(player.getPosition()) == 1.0,
                "С диагонали враг должен встать вплотную, получено расстояние "
                        + next.distanceTo(player.getPosition()));
    }

    @Test
    @DisplayName("Враг атакует только ортогонально, как и игрок")
    void enemyAttacksOnlyOrthogonally() {
        Player player = new Player(new Position(10, 10));

        Zombie orthogonal = new Zombie(new Position(10, 11), );
        assertTrue(orthogonal.canAttack(player), "Соседняя клетка - атака возможна");

        Zombie diagonal = new Zombie(new Position(11, 11), );
        assertFalse(diagonal.canAttack(player),
                "По диагонали игрок ответить не может, значит и враг бить не должен");

        Zombie far = new Zombie(new Position(10, 13), );
        assertFalse(far.canAttack(player), "Издалека атаки нет");
    }

    @Test
    @DisplayName("Враги не наступают на ключи и не затирают их")
    void enemiesDoNotStepOnKeys() {
        DungeonGenerator gen = new DungeonGenerator(DifficultyType.EASY);
        gen.generateDungeon();

        assertFalse(gen.getKeys().isEmpty(), "Для проверки нужен хотя бы один ключ");
        gen.getKeys().forEach(key ->
                assertTrue(DungeonGenerator.isPickupTile(
                                gen.getMap()[key.getPosition().getX()][key.getPosition().getY()]),
                        "Клетка ключа должна считаться подбираемой, иначе враг её затрёт"));
    }
}
