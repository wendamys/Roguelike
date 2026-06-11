package domain.navigator.interfaces;

public interface Distance {
    /**
     * метод {@link #distanceTo(Position)} вычисляет дистанцию между двумя объектами
     *
     * @param other позиция второго объекта
     * @return дистанция до объекта
     */
//    default double distanceTo(Position other) {
//        if (other == null) {
//            throw new NullPointerException("Other position cannot be null");
//        }
//        int dx = this.getX() - other.getX();
//        int dy = this.getY() - other.getY();
//        return Math.sqrt(dx * dx + dy * dy);
//    }
}