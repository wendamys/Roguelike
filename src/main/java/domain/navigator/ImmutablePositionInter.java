package domain.navigator;

import domain.navigator.interfaces.MovementDir;
import domain.navigator.interfaces.PositionInter;

public final class ImmutablePositionInter implements PositionInter, MovementDir {
    private final int x;
    private final int y;

    public ImmutablePositionInter(int x, int y) {
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
    public ImmutablePositionInter moveDir(DirectionType direction, int distance) {
        return switch (direction) {
            case FORWARD -> new ImmutablePositionInter(x, y + distance);
            case DOWN -> new ImmutablePositionInter(x, y - distance);
            case LEFT -> new ImmutablePositionInter(x - distance, y);
            case RIGHT -> new ImmutablePositionInter(x + distance, y);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionInter enemy)) return false;
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
