package domain.characters;

import domain.characters.enemies.EnemiesType;
import domain.characters.enemies.interfaces.RandomDirection;
import domain.navigator.DirectionType;
import domain.navigator.ImmutableDistance;
import domain.navigator.ImmutablePosition;
import domain.navigator.interfaces.Convergence;
import domain.navigator.interfaces.MovementRandom;
import domain.navigator.interfaces.Position;
import domain.MathUtils.RandomNumber;

abstract public class Enemies extends Character implements RandomDirection, MovementRandom, Convergence {

    private final EnemiesType type;
    private final int hostility;

    public Enemies(String name, EnemiesType type, int hostility, int health, int agility, int strength, Position position) {
        super(name, health, agility, strength, position);
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
    @Override
    public void moveRandom(int distance) {
        Position currentPos = getPosition();
        ImmutablePosition pos = new ImmutablePosition(currentPos.getX(), currentPos.getY());
        ImmutablePosition newPos = pos.moveDir(randomDirection(), 1);
        setPosition(newPos);
    }
    @Override
    public void convergence(Position positionPlayer, int distance) {
        Position currentPos = getPosition();
        ImmutableDistance dis = new ImmutableDistance(positionPlayer.getX(), positionPlayer.getY());
        double range = dis.distanceTo(currentPos);
        System.out.println(range);
        ImmutablePosition positionEnemy = new ImmutablePosition(currentPos.getX(), currentPos.getY());
//        ImmutablePosition positionForward = new ImmutablePosition(currentPos.getX(), currentPos.getY() - 1);
//        ImmutablePosition positionDown = new ImmutablePosition(currentPos.getX(), currentPos.getY() + 1);
//        ImmutablePosition positionRight = new ImmutablePosition(currentPos.getX() - 1, currentPos.getY());
//        ImmutablePosition positionLeft = new ImmutablePosition(currentPos.getX() + 1, currentPos.getY());
        ImmutablePosition posFor = positionEnemy.moveDir(DirectionType.FORWARD, 1);
        ImmutableDistance distanceForward = new ImmutableDistance(posFor.getX(), posFor.getY());

        double rangeForward = distanceForward.distanceTo(currentPos);
        System.out.println(rangeForward);
    }
}
