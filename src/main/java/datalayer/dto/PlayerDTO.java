package datalayer.dto;

import java.util.ArrayList;

public class PlayerDTO {
    private String name;
    private int maxHealth;
    private int health;
    private int buffAgility;
    private int buffStrength;
    private int currentWeaponValue;
    private int gold;
    private boolean isStunned;
    private PositionDTO positionDTO;
    private ArrayList<String> keys = new ArrayList<>();

    public PlayerDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public int getBuffAgility() { return buffAgility; }
    public void setBuffAgility(int buffAgility) { this.buffAgility = buffAgility; }

    public int getBuffStrength() { return buffStrength; }
    public void setBuffStrength(int buffStrength) { this.buffStrength = buffStrength; }

    public int getCurrentWeaponValue() {return currentWeaponValue;}
    public void setCurrentWeaponValue(int currentWeaponValue) {this.currentWeaponValue = currentWeaponValue;}

    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }

    public boolean getIsStunned() { return isStunned; }
    public void setIsStunned(boolean stunned) { isStunned = stunned; }

    public PositionDTO getPositionDTO() { return positionDTO; }
    public void setPositionDTO(PositionDTO positionDTO) { this.positionDTO = positionDTO; }

    public ArrayList<String> getKeys() { return keys; }
    public void setKeys(ArrayList<String> keys) { this.keys = keys; }
}
