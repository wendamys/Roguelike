package domain.map;

import domain.characters.Player;
import domain.gameSession.DifficultyType;
import domain.navigator.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FogOfWarTest {

    private static final int WIDTH = 70;
    private static final int HEIGHT = 60;

    /**
     * карта из сплошных стен с горизонтальным коридором пола в строке y
     */
    private TileType[][] mapWithCorridor(int y, int fromX, int toX) {
        TileType[][] map = new TileType[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int yy = 0; yy < HEIGHT; yy++) {
                map[x][yy] = TileType.WALL;
            }
        }
        for (int x = fromX; x <= toX; x++) {
            map[x][y] = TileType.FLOOR;
        }
        return map;
    }

    private TileType[][] allFloor() {
        TileType[][] map = new TileType[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                map[x][y] = TileType.FLOOR;
            }
        }
        return map;
    }

    @Test
    @DisplayName("На EASY видно всю карту")
    void easyRevealsEverything() {
        FogOfWar fog = new FogOfWar(WIDTH, HEIGHT);
        fog.update(new Player(new Position(5, 5)), List.of(), DifficultyType.EASY, allFloor());

        assertTrue(fog.isVisible(0, 0));
        assertTrue(fog.isVisible(69, 59));
        assertTrue(fog.isExplored(69, 59));
    }

    @Test
    @DisplayName("На VERY HARD видно только радиус 3 вокруг игрока")
    void veryHardLimitsToRadiusThree() {
        FogOfWar fog = new FogOfWar(WIDTH, HEIGHT);
        fog.update(new Player(new Position(20, 20)), List.of(), DifficultyType.VERY_HARD, allFloor());

        assertTrue(fog.isVisible(20, 20));
        assertTrue(fog.isVisible(23, 23));
        assertFalse(fog.isVisible(24, 20));
        assertFalse(fog.isVisible(20, 24));
    }

    @Test
    @DisplayName("На VERY HARD не видно стен дальше первой линии")
    void veryHardHidesWallsBehindWalls() {
        // пол тянется по строке y=10, всё остальное - сплошная стена
        TileType[][] map = mapWithCorridor(10, 10, 20);
        FogOfWar fog = new FogOfWar(WIDTH, HEIGHT);

        fog.update(new Player(new Position(15, 10)), List.of(), DifficultyType.VERY_HARD, map);

        assertTrue(fog.isVisible(15, 10), "Клетка под игроком видна");
        assertTrue(fog.isVisible(15, 9), "Стена вплотную к полу видна");
        assertTrue(fog.isVisible(15, 11), "Стена вплотную снизу видна");

        assertFalse(fog.isVisible(15, 8), "Стена за первой линией не видна");
        assertFalse(fog.isVisible(15, 7), "И тем более глубже");
        assertFalse(fog.isVisible(15, 12), "Снизу тоже только первая линия");
    }

    @Test
    @DisplayName("На VERY HARD память карты не накапливается")
    void veryHardHasNoMemory() {
        FogOfWar fog = new FogOfWar(WIDTH, HEIGHT);
        TileType[][] map = allFloor();
        fog.update(new Player(new Position(20, 20)), List.of(), DifficultyType.VERY_HARD, map);
        fog.update(new Player(new Position(40, 40)), List.of(), DifficultyType.VERY_HARD, map);

        assertFalse(fog.isExplored(20, 20), "Старая позиция не должна оставаться разведанной");
        assertTrue(fog.isExplored(40, 40));
    }

    @Test
    @DisplayName("На HARD вход в комнату открывает её целиком")
    void hardRevealsWholeRoom() {
        FogOfWar fog = new FogOfWar(WIDTH, HEIGHT);
        Room room = new Room(10, 10);
        Position centre = room.getCentreRoom();

        fog.update(new Player(centre), List.of(room), DifficultyType.HARD, allFloor());

        assertTrue(fog.isVisible(room.getPosition().getX(), room.getPosition().getY()));
        assertTrue(fog.isVisible(
                room.getPosition().getX() + room.getWidth() - 1,
                room.getPosition().getY() + room.getHeight() - 1));
    }

    @Test
    @DisplayName("На HARD покинутая комната остаётся разведанной, но невидимой")
    void hardKeepsMemory() {
        FogOfWar fog = new FogOfWar(WIDTH, HEIGHT);
        Room room = new Room(10, 10);
        Position centre = room.getCentreRoom();
        TileType[][] map = allFloor();

        fog.update(new Player(centre), List.of(room), DifficultyType.HARD, map);
        fog.update(new Player(new Position(60, 55)), List.of(room), DifficultyType.HARD, map);

        assertTrue(fog.isExplored(centre.getX(), centre.getY()));
        assertFalse(fog.isVisible(centre.getX(), centre.getY()));
    }
}
