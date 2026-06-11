package domain.navigator;

/**
 * Интерфейс задает направление движения и дистанцию, на которую передвигается объект
 */
public interface Position {
    int getX();

    int getY();

    /**
     * метод {@link #move(Direction, int)} перемещает персонажа на определенную дистанцию по игровому полю
     *
     * @param direction Направление движения
     * @param distance  дистанция перемещения
     * @return объект с координатами (x, y)
     */
    Position move(Direction direction, int distance);

    /**
     * метод {@link #distanceTo(Position)} вычисляет дистанцию между двумя объектами
     *
     * @param other позиция второго объекта
     * @return дистанция до объекта
     */
    default double distanceTo(Position other) {
        if (other == null) {
            throw new NullPointerException("Other position cannot be null");
        }
        int dx = this.getX() - other.getX();
        int dy = this.getY() - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}