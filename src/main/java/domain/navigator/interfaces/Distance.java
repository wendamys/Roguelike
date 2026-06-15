package domain.navigator.interfaces;

public interface Distance {
    /**
     * метод {@link #distanceTo(PositionInter)} вычисляет дистанцию между двумя объектами
     *
     * @param other позиция второго объекта
     * @return дистанция до объекта
     */
    double distanceTo(PositionInter other);
}