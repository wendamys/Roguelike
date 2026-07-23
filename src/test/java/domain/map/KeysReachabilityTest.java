package domain.map;

import domain.gameSession.DifficultyType;
import domain.navigator.DirectionType;
import domain.navigator.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Проверяет, что уровень реально проходится: каждый ключ достижим, если собирать
 * их по цепочке от стартовой комнаты. Ловит дедлок, когда ключ заперт за дверью,
 * ключ от которой лежит за этой же дверью.
 */
public class KeysReachabilityTest {

    @RepeatedTest(50)
    @DisplayName("Все ключи и все запертые комнаты достижимы от старта")
    void everyKeyIsReachable() {
        DungeonGenerator gen = new DungeonGenerator(DifficultyType.EASY);
        gen.generateDungeon();

        Position start = gen.getRooms().getFirst().getCentreRoom();
        Set<ColorKey> held = new HashSet<>();
        List<Key> remaining = new ArrayList<>(gen.getKeys());

        // Симулируем прохождение: пока есть прогресс, подбираем всё, до чего дошли
        boolean progress = true;
        while (progress) {
            progress = false;
            Set<Position> reach = flood(gen, start, held);
            for (Key key : new ArrayList<>(remaining)) {
                if (contains(reach, key.getPosition())) {
                    held.add(key.getColorKey());
                    remaining.remove(key);
                    progress = true;
                }
            }
        }

        assertTrue(remaining.isEmpty(),
                "Недостижимые ключи: " + remaining + ", собрано: " + held);

        // и сами запертые комнаты должны открываться
        Set<Position> finalReach = flood(gen, start, held);
        for (Room room : gen.getLockedRooms()) {
            assertTrue(contains(finalReach, room.getCentreRoom()),
                    "Запертая комната недостижима даже со всеми ключами: " + room.getPosition());
        }
    }

    @RepeatedTest(20)
    @DisplayName("Выход на следующий уровень достижим")
    void levelExitIsReachable() {
        DungeonGenerator gen = new DungeonGenerator(DifficultyType.EASY);
        gen.generateDungeon();

        Position start = gen.getRooms().getFirst().getCentreRoom();
        Set<ColorKey> held = new HashSet<>();
        for (Key key : gen.getKeys()) {
            held.add(key.getColorKey());
        }

        Position exit = gen.getRooms().getLast().getCentreRoom();
        assertTrue(contains(flood(gen, start, held), exit),
                "Выход на следующий уровень недостижим даже со всеми ключами");
    }

    /**
     * заливка проходимых клеток: двери открываются только имеющимися ключами
     */
    private Set<Position> flood(DungeonGenerator gen, Position start, Set<ColorKey> held) {
        Set<Position> seen = new HashSet<>();
        Deque<Position> queue = new ArrayDeque<>();
        queue.add(start);
        seen.add(start);

        while (!queue.isEmpty()) {
            Position cur = queue.poll();
            for (DirectionType dir : DirectionType.values()) {
                Position next = cur.posDir(dir);
                if (!gen.isInBounds(next.getX(), next.getY())) continue;
                if (contains(seen, next)) continue;

                TileType tile = gen.getMap()[next.getX()][next.getY()];
                if (tile == TileType.WALL) continue;

                ColorKey doorColor = DungeonGenerator.colorOfDoorTile(tile);
                if (doorColor != null && !held.contains(doorColor)) continue;

                seen.add(next);
                queue.add(next);
            }
        }
        return seen;
    }

    private boolean contains(Set<Position> positions, Position target) {
        return positions.stream().anyMatch(target::equals);
    }
}
