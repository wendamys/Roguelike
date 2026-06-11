package domain.navigator;

import domain.navigator.interfaces.Movement;
import domain.navigator.interfaces.Position;

public final class ImmutablePosition implements Position, Movement {
    private int x;
    private int y;

    public ImmutablePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public Position move(DirectionType direction, int distance) {
        return null;
    }

    @Override
    public ImmutablePosition move(DirectionType direction) {
        return switch (direction) {
            case FORWARD -> new ImmutablePosition(x, y++);
            case DOWN -> new ImmutablePosition(x, y--);
            case LEFT -> new ImmutablePosition(x--, y);
            case RIGHT -> new ImmutablePosition(x++, y);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position enemy)) return false;
        return x == enemy.getX() && y == enemy.getY();
    }

    @Override
    public int hashCode() {
        return x * 15 + y;
    }

    @Override
    public String toString() {
        return String.format("Position(%d, %d)", x, y);
    }
}
