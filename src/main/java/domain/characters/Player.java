package domain.characters;

import domain.backpack.*;
import domain.backpack.items.*;
import domain.navigator.Position;

/**
 * класс {@link #Player} описывает поведение игрока
 */
public class Player extends Character {

    private int gold = 0;

    private int maxHealth;
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

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
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

    public void useItemValue(Item item) {
        switch (item.getType()) {
            case FOOD -> useFoodValue((Food) item);
            case ELIXIR -> useElixirValue((Elixir) item);
            case SCROLL -> useScrollValue((Scroll) item);
            case WEAPON -> useWeaponValue((Weapon) item);
        }
    }

    private void useFoodValue(Food food) {
        upHealth = Math.min(upHealth + food.getValue(), maxHealth);
    }

    private void useScrollValue(Scroll scroll) {
        switch (scroll.getSubType()) {
            case HEALTH -> maxHealth += scroll.getValue();
            case AGILITY -> upAgility += scroll.getValue();
            case STRENGTH -> upStrength += scroll.getValue();
        }
    }

    private void useElixirValue(Elixir elixir) {
        switch(elixir.getSubType()) {
            case HEALTH -> upHealth = Math.min(upHealth + elixir.getValue(), maxHealth);
            case AGILITY -> upAgility += elixir.getValue();
            case STRENGTH -> upStrength += elixir.getValue();
        }
    }

    private void useWeaponValue(Weapon weapon) {
        upStrength += weapon.getValue();
    }

    @Override
    public String toString() {
        return String.format("Player:\nmaxHealth: %d\nHealth: %d\nupHealth: %d\nAgility: %d\nupAgility: %d\nStrength: %d\nUpStrength: %d",
                getMaxHealth(),
                getHealth(),
                getUpHealth(),
                getAgility(),
                getUpAgility(),
                getStrength(),
                getUpStrength()
                );
    }
}