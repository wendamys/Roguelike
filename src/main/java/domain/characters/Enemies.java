package domain.characters;

import domain.characters.enemies.EnemiesType;
import domain.navigator.DirectionType;
import domain.navigator.MovementSystem;
import domain.navigator.Position;


abstract public class Enemies extends Character {

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


}
