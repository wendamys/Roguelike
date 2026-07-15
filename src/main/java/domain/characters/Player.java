package domain.characters;

import domain.backpack.Item;
import domain.backpack.items.Elixir;
import domain.backpack.items.Food;
import domain.backpack.items.Scroll;
import domain.backpack.items.Weapon;
import domain.navigator.Position;

/**
 * класс {@link #Player} описывает поведение игрока
 */
public class Player extends Character {

    private String name;
    private int maxHealth = 500;

    // Изменяемые характеристики героя
    private int health = maxHealth;
    private int buffAgility = getAgility();
    private int buffStrength = getStrength();
    private int gold = 0;
    private boolean isStunned = false;

    public Player(Position position) {
        super(position);
    }

    public int getAgility() {
        return 70;
    }

    @Override
    public int getStrength() {
        return 70;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getBuffStrength() {
        return buffStrength;
    }

    public void setBuffStrength(int strength) {
        this.buffStrength = getStrength() + strength;
    }

    public int getBuffAgility() {
        return buffAgility;
    }

    public void setBuffAgility(int agility) {
        this.buffAgility = getAgility() + agility;
    }

    public void setUpHealthRegen(int regen) {
        this.health = Math.min((getHealth() + regen), maxHealth);
    }
    
    public boolean isStunned() {
        return isStunned;
    }
    
    public void setStunned(boolean stunned) {
        isStunned = stunned;
    }

    /**
     * метод юзает предмет и добавляет вэлью предмета игроку
     * @param item предмет
     */
    public void useItemValue(Item item) {
        switch (item.getType()) {
            case FOOD -> useFoodValue((Food) item);
            case ELIXIR -> useElixirValue((Elixir) item);
            case SCROLL -> useScrollValue((Scroll) item);
            case WEAPON -> useWeaponValue((Weapon) item);
        }
    }

    /**
     * метод расчитывает велью предмета еды
     * @param food предмет еды
     */
    private void useFoodValue(Food food) {
        health = Math.min(health + food.getValue(), maxHealth);
    }

    /**
     * метод расчитывает велью свитков
     * @param scroll предмет свитков
     */
    private void useScrollValue(Scroll scroll) {
        switch (scroll.getSubType()) {
            case HEALTH -> maxHealth += scroll.getValue();
            case AGILITY -> buffAgility += scroll.getValue();
            case STRENGTH -> buffStrength += scroll.getValue();
        }
    }

    /**
     * метод рассчитывает value эликсиров
     * @param elixir предметов эликсиров
     */
    private void useElixirValue(Elixir elixir) {
        switch (elixir.getSubType()) {
            case HEALTH -> health = Math.min(health + elixir.getValue(), maxHealth);
            case AGILITY -> buffAgility += elixir.getValue();
            case STRENGTH -> buffStrength += elixir.getValue();
        }
    }

    /**
     * метод рассчитывает value оружия
     * @param weapon предмет оружия
     */
    private void useWeaponValue(Weapon weapon) {
        buffStrength += weapon.getValue();
    }

    @Override
    public String toString() {
        return String.format("Health: %d/%d Agility: %d Strength: %d Gold: %d", getHealth(), getMaxHealth(), getBuffAgility(), getBuffStrength(), getGold());
    }


}