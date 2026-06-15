package domain;

import domain.characters.player.Player;
import domain.navigator.DirectionType;
import domain.navigator.ImmutablePositionInter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ImmutablePositionInterForPlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        ImmutablePositionInter startPosition = new ImmutablePositionInter(10, 20);
        player = new Player("Hero", 100, 100, 100, 100, 0, startPosition);
    }

    @Test
    void moveForwardShouldIncreaseYCoordinatePlayer() {
        ImmutablePositionInter expectedPosition = new ImmutablePositionInter(10, 21);
        player.move(DirectionType.FORWARD, 1);
        assertEquals(expectedPosition.getY(), player.getPosition().getY(), "Позиция после FORWARD должна измениться только по Y");
    }

    @Test
    void moveDownYCoordinatePlayer() {
        ImmutablePositionInter expectedPosition = new ImmutablePositionInter(10, 19);
        player.move(DirectionType.DOWN, 1);
        assertEquals(expectedPosition.getY(), player.getPosition().getY(), "Позиция после DOWN должна измениться только по Y");
    }

    @Test
    void moveLeftXCoordinatePlayer() {
        ImmutablePositionInter expectedPosition = new ImmutablePositionInter(9, 20);
        player.move(DirectionType.LEFT, 1);
        assertEquals(expectedPosition.getX(), player.getPosition().getX(), "Позиция после LEFT должна измениться только по X");
    }

    @Test
    void moveRightXCoordinatePlayer() {
        ImmutablePositionInter expectedPosition = new ImmutablePositionInter(11, 20);
        player.move(DirectionType.RIGHT, 1);
        assertEquals(expectedPosition.getX(), player.getPosition().getX(), "Позиция после RIGHT должна измениться только по X");
    }
}
