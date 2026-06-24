package domain.navigator;

import domain.characters.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class PositionForPlayerTest {

    private Player player;
    MovementSystem mv = new MovementSystem();


    @BeforeEach
    void setUp() {
        Position startPosition = new Position(10, 20);
        player = new Player(startPosition);
    }

    @Test
    void moveForwardShouldIncreaseYCoordinatePlayer() {
        Position expectedPosition = new Position(10, 21);
        mv.moveDir(DirectionType.FORWARD, player);
        assertEquals(expectedPosition.getY(), player.getPosition().getY(), "Позиция после FORWARD должна измениться только по Y");
    }

    @Test
    void moveDownYCoordinatePlayer() {
        Position expectedPosition = new Position(10, 19);
        mv.moveDir(DirectionType.DOWN, player);
        assertEquals(expectedPosition.getY(), player.getPosition().getY(), "Позиция после DOWN должна измениться только по Y");
    }

    @Test
    void moveLeftXCoordinatePlayer() {
        Position expectedPosition = new Position(9, 20);
        mv.moveDir(DirectionType.LEFT, player);
        assertEquals(expectedPosition.getX(), player.getPosition().getX(), "Позиция после LEFT должна измениться только по X");
    }

    @Test
    void moveRightXCoordinatePlayer() {
        Position expectedPosition = new Position(11, 20);
        mv.moveDir(DirectionType.RIGHT, player);
        assertEquals(expectedPosition.getX(), player.getPosition().getX(), "Позиция после RIGHT должна измениться только по X");
    }
}
