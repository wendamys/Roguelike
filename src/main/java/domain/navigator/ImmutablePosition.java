package domain.navigator;

public final class ImmutablePosition implements Position {
    private final int x;
    private final int y;

    ImmutablePosition(int x, int y) {
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
    public Position move(Direction direction, int distance) {
        return switch (direction) {
            case FORWARD -> new ImmutablePosition(x, y - distance);
            case DOWN -> new ImmutablePosition(x, y + distance);
            case LEFT -> new ImmutablePosition(x - distance, y);
            case RIGHT -> new ImmutablePosition(x + distance, y);
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
