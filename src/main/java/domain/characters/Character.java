package domain.characters;

import domain.navigator.Position;

/**
 * Абстрактный класс {@link #Character} описывает главные характеристики персонажей в игре
 * {@link #name} - имя персонажа
 * {@link #health} - очки жизни
 * {@link #agility} - ловкость
 * {@link #strength} - сила
 * {@link #position} - координаты позиции (x, y)
 */
public abstract class Character {
    private String name = null;
    private int health = 100;
    private final int agility = 30;
    private final int strength = 30;
    private Position position;
    protected int step = 1;

    public Character(Position position) {
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public int getStrength() {
        return strength;
    }

    public Position getPosition() {
        return position;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


    public void attack(Player player) {

    }


}
