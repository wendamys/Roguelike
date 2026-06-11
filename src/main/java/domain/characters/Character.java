package domain.characters;

import domain.navigator.Position;

/**
 * Абстрактный класс {@link #Character} описывает главные характеристики персонажей в игре
 * {@link #name} - имя персонажа
 * {@link #health} - очки жизни
 * {@link #agility} - ловкость
 * {@link #strength} - сила
 * {@link #isAlive} - состояние жизни/смерти
 * {@link #position} - координаты позиции (x, y)
 */
public abstract class Character {
    private final String name;
    private int health;
    private final int agility;
    private final int strength;
    private boolean isAlive;
    private final Position position;

    public Character(String name, int health, int agility, int strength, boolean isAlive, Position position) {
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
        this.isAlive = true;
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

    public boolean getIsAlive() {
        return isAlive;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * метод {@link #acceptDamage(int)} описывает получение урона персонажем.
     * Если {@link #health} опускается до 0 или ниже наступает смерть
     *
     * @param damage очки урона
     */
    public void acceptDamage(int damage) {
        if (!isAlive) return;

        int actualDamage = Math.max(0, damage);
        int newHealth = health - actualDamage;

        if (newHealth <= 0) {
            health = 0;
            isAlive = false;
        } else {
            health = newHealth;
        }
    }
}
