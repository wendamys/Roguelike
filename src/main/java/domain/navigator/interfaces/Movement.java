package domain.navigator.interfaces;

import domain.navigator.Direction;

public interface Movement {
    /**
     * метод {@link #move(Direction, int)} перемещает персонажа на определенную дистанцию по игровому полю
     *
     * @param direction Направление движения
     * @param distance  дистанция перемещения
     * @return объект с координатами (x, y)
     */
    Position move(Direction direction, int distance);
}