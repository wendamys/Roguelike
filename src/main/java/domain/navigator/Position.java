package domain.navigator;

public class Position {
    private final int x;
    private final int y;
    private boolean isClose = false;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {return x;}

    public int getY() { return y;}

    public boolean getIsClose() {
        return isClose;
    }

    public void setIsClose(boolean close) {
        isClose = close;
    }

    /**
     * Метод {@link #distanceTo(Position)} вычисляет дистанцию между двумя объектами
     *
     * @param other позиция второго объекта
     * @return дистанция до объекта
     */
    public double distanceTo(Position other) {
        if (other == null) {
            throw new NullPointerException("Other position cannot be null");
        }
        int dx = getX() - other.getX();
        int dy = getY() - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Метод {@link #posDir(DirectionType direction)} создает новую позицию
     * в зависимости от того направления, которое прислали
     *
     * @param direction Направление движения
     * @return Position
     */
    public Position posDir(DirectionType direction) {
        int x = getX();
        int y = getY();
        return switch (direction) {
            case FORWARD -> new Position(x, y + 1);
            case DOWN -> new Position(x, y - 1);
            case LEFT -> new Position(x - 1, y);
            case RIGHT -> new Position(x + 1, y);
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
}
