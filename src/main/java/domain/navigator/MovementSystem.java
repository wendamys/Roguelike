package domain.navigator;

import domain.characters.Character;
import domain.characters.Enemies;
import domain.characters.Player;

import static domain.MathUtils.MathUtils.randomNumber;

public class MovementSystem {

    protected DirectionType direction;

    /**
     * Метод {@link #randomDirection()} выбирает рандомно направление движения
     *
     * @return Направление движения
     */
    public DirectionType randomDirection() {
        return switch (randomNumber(1, 4)) {
            case 1 -> DirectionType.FORWARD; case 2 -> DirectionType.DOWN; case 3 -> DirectionType.RIGHT;
            case 4 -> DirectionType.LEFT; default -> throw new IllegalArgumentException("Error num randomDirection");
        };
    }

    /**
     * Метод {@link #moveDir(DirectionType direction, Character characte)} ходит по заданному направлению
     *
     * @param direction Направление движения
     * @param character Меняет текущую позицию переданному объекту
     */
    public void moveDir(DirectionType direction, Character character) {
        int x = character.getPosition().getX(); int y = character.getPosition().getY(); switch (direction) {
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

    public void EnemyGameMove(Player player, Enemies enemy) {
        while (enemy.getHealth() > 0) {
            if (enemy.convergenceIsHostility(player) == null) moveRandom(enemy);
            else moveDir(enemy.convergenceIsHostility(player), enemy);
        }

//        public void CheckContactEnemyToPlayer () {
//            {
//            }
//        }
    }
}
