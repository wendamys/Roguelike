package domain.navigator;

public class MovementSystem {

    public Position moveDir(DirectionType direction, Position position,  int distance) {
        int x = position.getX();
        int y = position.getY();
        return switch (direction) {
            case FORWARD -> new Position(x, y + distance);
            case DOWN -> new Position(x, y - distance);
            case LEFT -> new Position(x - distance, y);
            case RIGHT -> new Position(x + distance, y);
        };
    }
}
