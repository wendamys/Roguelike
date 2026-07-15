package domain.navigator;

import java.util.Random;

/**
 * Направление движения
 */
public enum DirectionType {
    FORWARD, DOWN, LEFT, RIGHT;

    private static final Random random = new Random();

    /**
     * метод {@link  #applyTo(Position)} применяет направление к позиции и возвращает новую позицию
     * @param position исходная позиция
     * @return новая позиция после применения направления
     */
    public Position applyTo(Position position) {
        return position.posDir(this);
    }

    /**
     * метод возвращает противоположное направление
     * @return противоположное направление
     */
    public DirectionType opposite() {
        return switch (this) {
            case FORWARD -> DirectionType.DOWN;
            case DOWN -> DirectionType.FORWARD;
            case LEFT -> DirectionType.RIGHT;
            case RIGHT -> DirectionType.LEFT;
        };
    }

    /**
     * Возвращает случайное направление
     * @return случайное направление
     */
    public static DirectionType random() {
        DirectionType[] values = values();
        return values[random.nextInt(values.length)];
    }
}