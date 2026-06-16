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
    private final String name;
    private int health;
    private final int agility;
    private final int strength;
    private Position position;

    public Character(String name, int health, int agility, int strength, Position position) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be positive");
        }
        if (agility < 0 || strength < 0) {
            throw new IllegalArgumentException("Stats cannot be negative");
        }
        this.name = name;
        this.health = health;
        this.agility = agility;
        this.strength = strength;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
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

    protected void setPosition(Position position) {
        this.position = position;
    }


    /**
    * Метод {@link #acceptDamage(int damage)} описывает получение урона персонажем.
    * Если урона больше чем здоровья то устанавливает здоровье 0
    *
    * @param damage очки урона
    */
    public void acceptDamage(int damage) {
        if (damage < 0 || health <= 0) return;
        int newHealth = health - damage;
        health = Math.max(newHealth, 0);
    }
}
