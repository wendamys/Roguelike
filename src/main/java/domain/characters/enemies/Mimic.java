package domain.characters.enemies;

import domain.characters.Enemies;
import domain.navigator.Position;

public class Mimic extends Enemies {

    private int health = 500;
    private int agility = 100;
    private int strength = 10;
    private final EnemiesType type = EnemiesType.MIMIK;

    public Mimic(Position position) {
        super(position);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public EnemiesType getType() {
        return type;
    }

    public String getName() {
        return "m";
    }

    @Override
    public String toString() {
        return String.format(
                "Mimik: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s",
                getName(),
                getHealth(),
                getAgility(),
                getStrength(),
                getType()
        );
    }
}
