package datalayer.dto;

public class EnemyDTO {
    private int health;
    private int maxHealth;
    private int agility;
    private int strength;
    private int hostility;
    private EnemyTypeDTO type;
    private boolean isInvisible;
    private boolean isStunned;
    private boolean isMimicking;
    private ItemsTypeDTO itemType;
    private ItemsSubTypeDTO itemSubType;
    private int itemValue;
    private PositionDTO position;

    public EnemyDTO() {}

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    public int getAgility() { return agility; }
    public void setAgility(int agility) { this.agility = agility; }

    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }

    public int getHostility() { return hostility; }
    public void setHostility(int hostility) { this.hostility = hostility; }

    public EnemyTypeDTO getType() { return type; }
    public void setType(EnemyTypeDTO type) { this.type = type; }

    public boolean getIsInvisible() { return isInvisible; }
    public void setIsInvisible(boolean isInvisible) { this.isInvisible = isInvisible; }

    public boolean getIsStunned() { return isStunned; }
    public void setIsStunned(boolean isStunned) { this.isStunned = isStunned; }

    public boolean getIsMimicking() { return isMimicking; }
    public void setIsMimicking(boolean isMimicking) { this.isMimicking = isMimicking; }

    public ItemsTypeDTO getItemType() { return itemType; }
    public void setItemType(ItemsTypeDTO itemType) { this.itemType = itemType; }

    public ItemsSubTypeDTO getItemSubType() { return itemSubType; }
    public void setItemSubType(ItemsSubTypeDTO itemSubType) { this.itemSubType = itemSubType; }

    public int getItemValue() { return itemValue; }
    public void setItemValue(int itemValue) { this.itemValue = itemValue; }

    public PositionDTO getPosition() { return position; }
    public void setPosition(PositionDTO position) { this.position = position; }
}
