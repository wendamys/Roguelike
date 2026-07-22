package domain.map;

import domain.gameSession.DifficultyType;
import domain.navigator.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DoorKeyTest {

    private DungeonGenerator generateDungeon() {
        DungeonGenerator generator = new DungeonGenerator(DifficultyType.EASY);
        generator.generateDungeon();
        return generator;
    }

    @RepeatedTest(10)
    @DisplayName("Все входы одной запертой комнаты имеют один цвет")
    void allEntrancesShareOneColor() {
        DungeonGenerator generator = generateDungeon();

        for (Room room : generator.getRooms()) {
            Door door = room.getDoor();
            if (door == null) {
                continue;
            }
            for (Position entrance : door.getEntrances()) {
                TileType tile = generator.getMap()[entrance.getX()][entrance.getY()];
                assertEquals(DungeonGenerator.doorTileFor(door.getColorKey()), tile,
                        "Вход " + entrance + " должен быть дверью цвета " + door.getColorKey());
            }
        }
    }

    @RepeatedTest(10)
    @DisplayName("Цвета запертых комнат не повторяются")
    void lockedRoomColorsAreUnique() {
        DungeonGenerator generator = generateDungeon();

        Set<ColorKey> seen = new HashSet<>();
        for (Room room : generator.getRooms()) {
            if (room.getDoor() != null) {
                assertTrue(seen.add(room.getDoor().getColorKey()),
                        "Цвет " + room.getDoor().getColorKey() + " использован дважды");
            }
        }
    }

    @RepeatedTest(10)
    @DisplayName("Стартовая комната никогда не заперта")
    void startRoomIsNeverLocked() {
        DungeonGenerator generator = generateDungeon();

        assertNull(generator.getRooms().getFirst().getDoor());
    }

    @RepeatedTest(10)
    @DisplayName("Закрытая дверь непроходима")
    void closedDoorIsNotWalkable() {
        DungeonGenerator generator = generateDungeon();

        for (Room room : generator.getRooms()) {
            if (room.getDoor() == null) {
                continue;
            }
            for (Position entrance : room.getDoor().getEntrances()) {
                assertFalse(generator.isPositionWalkable(entrance));
            }
        }
    }

    @RepeatedTest(10)
    @DisplayName("Ключи образуют цепочку: ключ каждой следующей комнаты лежит в предыдущей")
    void keysFormReachableChain() {
        DungeonGenerator generator = generateDungeon();

        List<Key> keys = generator.getKeys();
        List<Room> locked = generator.getLockedRooms();
        assertEquals(locked.size(), keys.size(), "На каждую запертую комнату один ключ");

        for (int i = 0; i < keys.size(); i++) {
            assertEquals(locked.get(i).getDoor().getColorKey(), keys.get(i).getColorKey());

            Room holder = i == 0 ? generator.getRooms().getFirst() : locked.get(i - 1);
            assertTrue(isInsideRoom(keys.get(i).getPosition(), holder),
                    "Ключ " + i + " должен лежать внутри комнаты-держателя");
        }
    }

    @RepeatedTest(10)
    @DisplayName("Ключ лежит на карте своим тайлом")
    void keysArePlacedOnMap() {
        DungeonGenerator generator = generateDungeon();

        for (Key key : generator.getKeys()) {
            Position pos = key.getPosition();
            assertEquals(DungeonGenerator.keyTileFor(key.getColorKey()),
                    generator.getMap()[pos.getX()][pos.getY()]);
        }
    }

    private boolean isInsideRoom(Position pos, Room room) {
        Position roomPos = room.getPosition();
        return pos.getX() >= roomPos.getX() && pos.getX() < roomPos.getX() + room.getWidth()
                && pos.getY() >= roomPos.getY() && pos.getY() < roomPos.getY() + room.getHeight();
    }
}
