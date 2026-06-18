package domain.characters;

import domain.backpack.ItemsSubType;
import domain.navigator.DirectionType;
import domain.navigator.Position;

/**
 * класс {@link #Player} описывает поведение игрока
 */
public class Player extends Character {

    private int gold;

    private final int maxHealth;
    private int upHealth;
    private int upAgility;
    private int upStrength;

    public Player(String name, int health, int agility, int strength, int maxHealth, int gold, Position position) {
        super(name, health, agility, strength, position);
        this.maxHealth = getHealth();
        this.upHealth = getHealth();
        this.upAgility = getAgility();
        this.upStrength = getStrength();
        this.gold = 0;
    }

    public int getGold() {
        return gold;
    }
    public int getMaxHealth() { return maxHealth; }
    public int getUpHealth() { return upHealth; }
    public int getUpStrength() { return upStrength; }
    public int getUpAgility() {
        return upAgility;
    }

    public void setUpHealth(int upHealth) {
        this.upHealth = upHealth;
    }

    public void setUpHealthRegen(int regen) {
        this.upHealth = Math.min((getUpHealth() + regen), maxHealth);
    }

    public void setUpAgility(int agility) {
        this.upAgility = getAgility() + agility;
    }

    public void setUpStrength(int strength) {
        this.upStrength = getStrength() + strength;
    }

    public void useItemValue(ItemsSubType subType, int value) {
        switch (subType) {
            case HEALTH -> setUpHealthRegen(value);
            case AGILITY -> setUpAgility(value);
            case STRENGTH -> setUpStrength(value);
        }
    }

    @Override
    public String toString() {
        return String.format("Player:\nHealth: %d\nupHealth: %d\nAgility: %d\nupAgility: %d\nStrength: %d\nUpStrength: %d",
                getHealth(),
                getUpHealth(),
                getAgility(),
                getUpAgility(),
                getStrength(),
                getUpStrength()
                );
    }
}