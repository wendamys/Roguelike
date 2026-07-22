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

    @Test
    @DisplayName("На EASY видно всю карту")
    void easyRevealsEverything() {
        FogOfWar fog = new FogOfWar(70, 60);
        fog.update(new Player(new Position(5, 5)), List.of(), DifficultyType.EASY);

        assertTrue(fog.isVisible(0, 0));
        assertTrue(fog.isVisible(69, 59));
        assertTrue(fog.isExplored(69, 59));
    }

    @Test
    @DisplayName("На VERY HARD видно только радиус 3 вокруг игрока")
    void veryHardLimitsToRadiusThree() {
        FogOfWar fog = new FogOfWar(70, 60);
        fog.update(new Player(new Position(20, 20)), List.of(), DifficultyType.VERY_HARD);

        assertTrue(fog.isVisible(20, 20));
        assertTrue(fog.isVisible(23, 23));
        assertFalse(fog.isVisible(24, 20));
        assertFalse(fog.isVisible(20, 24));
    }

    @Test
    @DisplayName("На VERY HARD память карты не накапливается")
    void veryHardHasNoMemory() {
        FogOfWar fog = new FogOfWar(70, 60);
        fog.update(new Player(new Position(20, 20)), List.of(), DifficultyType.VERY_HARD);
        fog.update(new Player(new Position(40, 40)), List.of(), DifficultyType.VERY_HARD);

        assertFalse(fog.isExplored(20, 20), "Старая позиция не должна оставаться разведанной");
        assertTrue(fog.isExplored(40, 40));
    }

    @Test
    @DisplayName("На HARD вход в комнату открывает её целиком")
    void hardRevealsWholeRoom() {
        FogOfWar fog = new FogOfWar(70, 60);
        Room room = new Room(10, 10);
        Position centre = room.getCentreRoom();

        fog.update(new Player(centre), List.of(room), DifficultyType.HARD);

        assertTrue(fog.isVisible(room.getPosition().getX(), room.getPosition().getY()));
        assertTrue(fog.isVisible(
                room.getPosition().getX() + room.getWidth() - 1,
                room.getPosition().getY() + room.getHeight() - 1));
    }

    @Test
    @DisplayName("На HARD покинутая комната остаётся разведанной, но невидимой")
    void hardKeepsMemory() {
        FogOfWar fog = new FogOfWar(70, 60);
        Room room = new Room(10, 10);
        Position centre = room.getCentreRoom();

        fog.update(new Player(centre), List.of(room), DifficultyType.HARD);
        fog.update(new Player(new Position(60, 55)), List.of(room), DifficultyType.HARD);

        assertTrue(fog.isExplored(centre.getX(), centre.getY()));
        assertFalse(fog.isVisible(centre.getX(), centre.getY()));
    }
}
