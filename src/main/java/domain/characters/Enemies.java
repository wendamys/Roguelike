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


    /**
    * Метод {@link #randomDirection()} выбирает рандомно направление движения
    *
    * @return Направление движения
    */
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

    /**
     * Метод {@link #moveRandom(int distance)} выбирает рандомно направление движения
     * и устанавливает его объекту, который его вызывал
     */
    public void moveRandom(int distance) {
        Position pos = new Position(getPosition().getX(), getPosition().getY());
        Position newPos = pos.moveDir(randomDirection(), distance);
        setPosition(newPos);
    }

    public DirectionType convergence(Position positionPlayer, int distance) {

        //  Создаем врага с текущий позицией
        Position enemyMove = new Position(getPosition().getX(), getPosition().getY());

        System.out.println("Coordinate player: " + positionPlayer.getX() + " " + positionPlayer.getY());
        System.out.println("Coordinate enemy: " + enemyMove.getX() + " " + enemyMove.getY());

        // Перебираем пути, находим минимальный, идем туда, если игрок близко возвращаем true
        double min = Double.MAX_VALUE;
        DirectionType dirMove = DirectionType.RIGHT;
        for (DirectionType dT : DirectionType.values()) {
            Position enemyPos = enemyMove.moveDir(dT, distance);
            double findRange = enemyPos.distanceTo(positionPlayer);
            if (findRange <= min) {
                min = findRange;
                dirMove = dT;
            }
            System.out.println("Range to player :" + findRange);
        }
        return dirMove;

        // if (min != 0.0) {
        //     setPosition(enemyMove.moveDir(dirMove, distance));
        //     return false;
        // } else {
        //     System.out.println("Attack player!");
        //     return true;
        // }
    }
}
