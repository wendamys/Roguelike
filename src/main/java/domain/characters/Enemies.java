package domain.characters;

import domain.characters.enemies.EnemiesType;
import domain.characters.enemies.interfaces.RandomDirection;
import domain.navigator.DirectionType;
import domain.navigator.interfaces.Position;

import java.util.Random;

abstract public class Enemies extends Character implements RandomDirection {

    private final EnemiesType type;
    private final int hostility;
    protected Random random = new Random();


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
        int randomNumber = random.nextInt(4) + 1;
        System.out.println(randomNumber);
        return switch (randomNumber) {
            case 1 -> DirectionType.FORWARD;
            case 2 -> DirectionType.DOWN;
            case 3 -> DirectionType.RIGHT;
            case 4 -> DirectionType.LEFT;
            default -> throw new IllegalArgumentException("Error num randomDirection");
        };
    }
}
