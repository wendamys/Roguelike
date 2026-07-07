package domain.navigator;

import domain.characters.Character;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.Ogre;

import static domain.MathUtils.MathUtils.randomDirection;

public class MovementSystem {

    /**
     * Метод {@link #moveDir(DirectionType direction, Character characte)} ходит по заданному направлению
     *
     * @param direction Направление движения
     * @param character Меняет текущую позицию переданному объекту
     */
    public void moveDir(DirectionType direction, Character character) {
        int x = character.getPosition().getX();
        int y = character.getPosition().getY();
        switch (direction) {
            case FORWARD -> character.setPosition(new Position(x, y + 1));
            case DOWN -> character.setPosition(new Position(x, y - 1));
            case LEFT -> character.setPosition(new Position(x - 1, y));
            case RIGHT -> character.setPosition(new Position(x + 1, y));
        }
    }

    /**
     * Метод {@link #moveRandom} выбирает рандомно направление движения
     * и вызывает метод moveDir, который ходит по заданному направлению
     */
    public void moveRandom(Character character) {
        moveDir(randomDirection(), character);
    }

    /**
     * Метод {@link #EnemyGameMove(Player player, Enemies enemy)} служит для того
     * чтобы моб ходил рандомно до того момента пока не игрок не попадет в его радиус
     *
     * @param player игрок
     * @param enemy противник
     */
    public void EnemyGameMove(Player player, Enemies enemy) {
        while (enemy.getHealth() > 0) {
            if (enemy.convergenceIsHostility(player) == null) moveRandom(enemy);
            else moveDir(enemy.convergenceIsHostility(player), enemy);
        }
    }
}
