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

    @Override
    public DirectionType randomDirection() {
        int rand = random.ints(1, 4).sum();
        System.out.println(123);
        return null;
    }
}
