package domain.characters.enemies;

import domain.characters.Enemies;
import domain.navigator.Position;

public class Snake extends Enemies {

    private int health = 150;
    private int agility = 40;
    private int strength = 40;
    private final EnemiesType type = EnemiesType.SNAKE;

    public Snake(Position position) {
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
    public String getName() {
        return "s";
    }

    @Override
    public EnemiesType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format(
                "Snake: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s",
                getName(),
                getHealth(),
                getAgility(),
                getStrength(),
                getType()
        );
    }
}
