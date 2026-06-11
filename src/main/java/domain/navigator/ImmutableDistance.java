package domain.navigator;

import domain.navigator.interfaces.Distance;
import domain.navigator.interfaces.Position;

public final class ImmutableDistance implements Position, Distance {
    private final int x;
    private final int y;

    public ImmutableDistance(int x, int y) {
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
    public double distanceTo(Position other) {
        if (other == null) {
            throw new NullPointerException("Other position cannot be null");
        }
        int dx = this.getX() - other.getX();
        int dy = this.getY() - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
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

//    @Override
//    public String toString() {
//        return String.format("Distance(%.2f)", ;
//    }
}