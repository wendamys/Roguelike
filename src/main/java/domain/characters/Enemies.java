package domain.characters;

import domain.characters.enemies.EnemiesType;
import domain.navigator.DirectionType;
import domain.navigator.Position;
import domain.MathUtils.RandomNumber;

abstract public class Enemies extends Character {

    private final EnemiesType type;
    private final int hostility;

    public Enemies(String name, EnemiesType type, int hostility, int health, int agility, int strength, Position positionInter) {
        super(name, health, agility, strength, positionInter);
        this.type = type;
        this.hostility = hostility;
    }

    public int getHostility() {
        return hostility;
    }

    public EnemiesType getType() {
        return type;
    }

    public DirectionType randomDirection() {
        RandomNumber randomDir = new RandomNumber();
        return switch (randomDir.randomNumber(1, 4)) {
            case 1 -> DirectionType.FORWARD;
            case 2 -> DirectionType.DOWN;
            case 3 -> DirectionType.RIGHT;
            case 4 -> DirectionType.LEFT;
            default -> throw new IllegalArgumentException("Error num randomDirection");
        };
    }

    public void moveRandom(int distance) {
        Position currentPos = getPosition();
        Position pos = new Position(currentPos.getX(), currentPos.getY());
        Position newPos = pos.moveDir(randomDirection(), 1);
        setPosition(newPos);
    }

    public void convergence(Position positionPlayer, int distance) {

        //  Создаем врага с текущий позицией
        Position enemyMove = new Position(getPosition().getX(), getPosition().getY());

        System.out.println("Coordinate player: " + positionPlayer.getX() + " " + positionPlayer.getY());
        System.out.println("Coordinate enemy: " + enemyMove.getX() + " " + enemyMove.getY());

        // Нужно просто указать переменные т.к. чтобы было видно и внутри фора и снаружи
        double min = Double.MAX_VALUE;
        DirectionType dirMove = DirectionType.RIGHT;
        // Тут пробегаемся по типам, которые лежат в DirectionType
        for (DirectionType dT: DirectionType.values()) {
            // Мы ходим в одном направлении, которое лежит в типах
            Position enemyPos = enemyMove.moveDir(dT, 1);
            // Сразу высчитываю расстояние от противника до игрока
            double findRange = enemyPos.distanceTo(positionPlayer);
            // Проверка на минимальное значение и запись минимального значения
            // для следующей сверки и запись куда нужно идти
            if (findRange <= min) {
                min = findRange;
                dirMove = dT;
            }
            System.out.println("Range to player :" + findRange);
        }
        setPosition(enemyMove.moveDir(dirMove, 1));
    }
}
