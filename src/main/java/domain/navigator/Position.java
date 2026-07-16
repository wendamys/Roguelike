package domain.navigator;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {return x;}
    public int getY() { return y;}


    /**
     * Метод вычисляет дистанцию между двумя объектами
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
     * Метод создает новую позицию в зависимости от того направления, которое прислали
     * @param direction Направление движения
     * @return Position
     */
    public Position posDir(DirectionType direction) {
        int x = getX();
        int y = getY();
        return switch (direction) {
            case FORWARD -> new Position(x, y - 1);
            case DOWN -> new Position(x, y + 1);
            case LEFT -> new Position(x - 1, y);
            case RIGHT -> new Position(x + 1, y);
        };
    }

    /**
     * Метод создает новую позицию, сдвинутую на одно клетку в заданном направлении
     * @param direction Направление движения
     * @return новая позиция
     * @deprecated используйте {@link DirectionType#applyTo(Position)}
     */
    @Deprecated
    public Position move(DirectionType direction) {
        return posDir(direction);
    }

    /**
     * метод копирует позицию
     * @return позиция
     */
    public Position copy() {
        return new Position(x, y);
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
        return String.format(
                "(%d, %d)",
                getX(),
                getY()
        );
    }
}
