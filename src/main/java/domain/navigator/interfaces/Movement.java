package domain.navigator.interfaces;

import domain.navigator.DirectionType;

public interface Movement {
    /**
     * метод {@link #move(DirectionType, int)} перемещает персонажа на определенную дистанцию по игровому полю
     *
     * @param direction Направление движения
     * @param distance  дистанция перемещения
     * @return объект с координатами (x, y)
     */
    Position move(DirectionType direction, int distance);
}