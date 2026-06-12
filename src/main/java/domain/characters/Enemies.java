package domain.characters;

import domain.characters.enemies.EnemiesType;
import domain.characters.enemies.interfaces.RandomDirection;
import domain.navigator.DirectionType;
import domain.navigator.ImmutablePosition;
import domain.navigator.interfaces.MovementRandom;
import domain.navigator.interfaces.Position;
import domain.MathUtils.RandomNumber;

abstract public class Enemies extends Character implements RandomDirection, MovementRandom {

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
}
