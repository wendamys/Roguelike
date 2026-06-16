package domain.navigator;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) { this.x = x; this.y = y; }
    public int getX() { return x; }
    public int getY() {
        return y;
    }

    /**
    * Метод {@link #moveDir(DirectionType, int distance)} выбирает направление передвижения персонажа
    * @param direction Направление движения
    * @param distance  дистанция перемещения
    * @return новый объект с координатами (x, y)
    */
    public Position moveDir(DirectionType direction, int distance) {
        return switch (direction) {
            case FORWARD -> new Position(x, y + distance);
            case DOWN -> new Position(x, y - distance);
            case LEFT -> new Position(x - distance, y);
            case RIGHT -> new Position(x + distance, y);
        };
    }
    /**
    * Метод {@link #distanceTo(Position)} вычисляет дистанцию между двумя объектами
    * @param other позиция второго объекта
    * @return дистанция до объекта
    */
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
