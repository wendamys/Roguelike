package domain.characters;

import domain.navigator.Position;

/**
 * Абстрактный класс {@link #Character} описывает главные характеристики персонажей в игре
 */
public abstract class Character {
    protected int step = 1;
    private String name = null;
    private int health = 100;
    private Position position;

    public Character(Position position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAgility() {
        return 30;
    }

    public int getStrength() {
        return 30;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
