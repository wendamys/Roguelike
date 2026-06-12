package domain.navigator.interfaces;

import domain.navigator.DirectionType;

public interface Movement {
    /**
     * метод {@link #move(Direction, int)} перемещает выбранного персонажа на дистанцию по игровому полю
     * @param direction направление движения
     * @param distance дистанция перемещения
     */
    void move(Direction direction, int distance);
}
