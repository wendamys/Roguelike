package domain;

import domain.characters.player.Player;
import domain.navigator.DirectionType;
import domain.navigator.ImmutablePosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ImmutablePositionTest {

    private Player player;

    @BeforeEach
    void setUp(){
        ImmutablePosition startPosition = new ImmutablePosition(10, 20);
        player = new Player("Hero", 100, 100, 100, 100, 0, startPosition);
    }

    @Test
    void moveForwardShouldIncreaseYCoordinatePlayer() {
        ImmutablePosition expectedPosition = new ImmutablePosition(10, 21);

        ImmutablePosition actualPosition = player.move(DirectionType.FORWARD);

        assertEquals(expectedPosition, actualPosition, "Позиция после FORWARD должна измениться только по Y");
    }

    @Test
    void moveDownYCoordinatePlayer() {
        ImmutablePosition expectedPosition = new ImmutablePosition(10, 19);

        ImmutablePosition actualPosition = player.move(DirectionType.DOWN);

        assertEquals(expectedPosition, actualPosition, "Позиция после DOWN должна измениться только по Y");
    }

    @Test
    void moveLeftXCoordinatePlayer() {
        ImmutablePosition expectedPosition = new ImmutablePosition(9, 20);

        ImmutablePosition actualPosition = player.move(DirectionType.LEFT);

        assertEquals(expectedPosition, actualPosition, "Позиция после LEFT должна измениться только по X");
    }

    @Test
    void moveRightXCoordinatePlayer() {
        ImmutablePosition expectedPosition = new ImmutablePosition(11, 20);

        ImmutablePosition actualPosition = player.move(DirectionType.RIGHT);

        assertEquals(expectedPosition, actualPosition, "Позиция после RIGHT должна измениться только по X");
    }


}
