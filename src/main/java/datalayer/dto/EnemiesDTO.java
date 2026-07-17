package datalayer.dto;

import domain.characters.enemies.EnemiesType;

public class EnemiesDTO {
    private EnemiesType type;
    private String name;
    private int health;
    private int maxHealth;
    private int agility;
    private int strength;
    private int hostility;

    private boolean isInvisible;
    private boolean isStunned;
    private boolean isMimicking;
    private PositionDTO positionDTO;

    public EnemiesDTO() {}

    public EnemiesType getType() {return type;}
    public void setType(EnemiesType type) {this.type = type;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getHealth() {return health;}
    public void setHealth(int health) {this.health = health;}

    public int getMaxHealth() {return maxHealth;}
    public void setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}

    public int getAgility() {return agility;}
    public void setAgility(int agility) {this.agility = agility;}

    public int getStrength() {return strength;}
    public void setStrength(int strength) {this.strength = strength;}

    public int getHostility() {return hostility;}
    public void setHostility(int hostility) {this.hostility = hostility;}

    public boolean isInvisible() {return isInvisible;}
    public void setInvisible(boolean invisible) {isInvisible = invisible;}

    public boolean isStunned() {return isStunned;}
    public void setStunned(boolean stunned) {isStunned = stunned;}

    public boolean isMimicking() {return isMimicking;}
    public void setMimicking(boolean mimicking) {isMimicking = mimicking;}

    public PositionDTO getPositionDTO() {return positionDTO;}
    public void setPositionDTO(PositionDTO positionDTO) {this.positionDTO = positionDTO;}
}
