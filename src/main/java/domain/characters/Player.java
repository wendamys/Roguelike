package domain.characters;

import domain.navigator.DirectionType;
import domain.navigator.Position;

/**
 * класс {@link #Player} описывает поведение игрока
 */
public class Player extends Character {

    private final int maxHealth;
    private int gold;
    private int upStrength;

    public Player(String name, int health, int agility, int strength, int maxHealth, int gold, Position position) {
        super(name, health, agility, strength, position);
        this.maxHealth = maxHealth;
        this.gold = 0;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getGold() {
        return gold;
    }

    public void setUpStrength(int upStrength, int strength) {
        this.upStrength = upStrength + strength;
    }

    public int getUpStrength() {
        return upStrength;
    }

    @Override
    public String toString() {
        return String.format("Player:\nStrength: %d\nUpStrength: %d",
                getStrength(),
                getUpStrength()
                );
    }
}