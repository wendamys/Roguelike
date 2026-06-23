package domain.characters;

import domain.backpack.*;
import domain.backpack.items.*;
import domain.navigator.Position;

/**
 * класс {@link #Player} описывает поведение игрока
 */
public class Player extends Character {

    private String name;
    private int maxHealth = 500;
    private int health = 500;

    private int upHealth = health;
    private int upAgility = getAgility();
    private int upStrength = getStrength();
    private int gold = 0;

    public Player(Position position) {
        super(position);
    }

    public int getAgility() {
        return 70;
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
    public int getStrength() {
        return 70;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGold() {
        return gold;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getUpHealth() {
        return upHealth;
    }

    public int getUpStrength() {
        return upStrength;
    }

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
        switch (elixir.getSubType()) {
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