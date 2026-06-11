package domain.characters.enemies.interfaces;

import domain.navigator.DirectionType;

public interface RandomDirection {

    /**
     * метод {@link #randomDirection()} выбирает рандомно направление движения
     *
     * @return направление движения
     */
    public DirectionType randomDirection();

}
