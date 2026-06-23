package domain.characters.enemies;

import domain.characters.Enemies;
import domain.navigator.Position;

public class Zombie extends Enemies {

    private int health = 100;
    private int agility = 30;
    private int strength = 30;
    private final EnemiesType type = EnemiesType.ZOMBIE;

    public Zombie(Position position) {
        super(position);
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public EnemiesType getType() {
        return type;
    }

    @Override
    public String getName() {
        return "z";
    }

    @Override
    public String toString() {
        return String.format(
                "Zombie: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s",
                getName(),
                getHealth(),
                getAgility(),
                getStrength(),
                getType()
        );
    }
}
