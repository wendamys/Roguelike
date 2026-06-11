package domain.navigator.interfaces;

public interface Distance {
    /**
     * метод {@link #distanceTo(Position)} вычисляет дистанцию между двумя объектами
     *
     * @param other позиция второго объекта
     * @return дистанция до объекта
     */
    double distanceTo(Position other);
}