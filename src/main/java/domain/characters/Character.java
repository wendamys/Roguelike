package domain.characters;

import domain.navigator.interfaces.PositionInter;

/**
 * Абстрактный класс {@link #Character} описывает главные характеристики персонажей в игре
 * {@link #name} - имя персонажа
 * {@link #health} - очки жизни
 * {@link #agility} - ловкость
 * {@link #strength} - сила
 * {@link #positionInter} - координаты позиции (x, y)
 */
public abstract class Character implements DamageDiller {
    private final String name;
    private int health;
    private final int agility;
    private final int strength;
    private PositionInter positionInter;

    public Character(String name, int health, int agility, int strength, PositionInter positionInter) {
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
        this.positionInter = positionInter;
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

    public PositionInter getPosition() {
        return positionInter;
    }

    protected void setPosition(PositionInter positionInter) {
        this.positionInter = positionInter;
    }

    @Override
    public void acceptDamage(int damage) {
        if (damage < 0 || health <= 0) return;
        int newHealth = health - damage;
        health = Math.max(newHealth, 0);
    }
}
