package domain;

import domain.characters.player.Player;
import domain.navigator.DirectionType;
import domain.navigator.ImmutablePosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ImmutablePositionForPlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        ImmutablePosition startPosition = new ImmutablePosition(10, 20);
        player = new Player("Hero", 100, 100, 100, 100, 0, startPosition);
    }

    @Test
    void moveForwardShouldIncreaseYCoordinatePlayer() {
        ImmutablePosition expectedPosition = new ImmutablePosition(10, 21);
        player.move(DirectionType.FORWARD, 1);
        assertEquals(expectedPosition.getY(), player.getPosition().getY(), "Позиция после FORWARD должна измениться только по Y");
    }

    @Test
    void moveDownYCoordinatePlayer() {
        ImmutablePosition expectedPosition = new ImmutablePosition(10, 19);
        player.move(DirectionType.DOWN, 1);
        assertEquals(expectedPosition.getY(), player.getPosition().getY(), "Позиция после DOWN должна измениться только по Y");
    }

    @Test
    void moveLeftXCoordinatePlayer() {
        ImmutablePosition expectedPosition = new ImmutablePosition(9, 20);
        player.move(DirectionType.LEFT, 1);
        assertEquals(expectedPosition.getX(), player.getPosition().getX(), "Позиция после LEFT должна измениться только по X");
    }

    @Test
    void moveRightXCoordinatePlayer() {
        ImmutablePosition expectedPosition = new ImmutablePosition(11, 20);
        player.move(DirectionType.RIGHT, 1);
        assertEquals(expectedPosition.getX(), player.getPosition().getX(), "Позиция после RIGHT должна измениться только по X");
    }
}
