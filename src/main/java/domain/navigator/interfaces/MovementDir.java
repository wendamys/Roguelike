package domain.navigator.interfaces;

import domain.navigator.Direction;

public interface MovementDir {
    /**
     * метод {@link #moveDir(Direction, int)} выбирает направление передвижения персонажа
     *
     * @param direction Направление движения
     * @param distance  дистанция перемещения
     * @return объект с координатами (x, y)
     */
    Position moveDir(Direction direction, int distance);
}