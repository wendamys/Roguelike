package domain.characters.enemies;

import domain.ai.*;
import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

public class Mimic extends Enemies {

    private final EnemiesType type = EnemiesType.MIMIC;
    private int health = 150;
    private int agility = 70;
    private int strength = 10;

    public Mimic(Position position) {
        super(position);
        super.setHealthBegin((int) (health * Level.getCoefEnemy()));
        super.setMaxHealth(super.getHealth());
        super.setAgilityRand((int) (agility * Level.getCoefEnemy()));
        super.setStrengthRand((int) (strength * Level.getCoefEnemy()));
    }

    @Override
    protected EnemyAI createAI() {
        return new AmbushAI();
    }
    
    public AmbushAI getAmbushAI() {
        return (AmbushAI) ai;
    }

    @Override
    public EnemiesType getType() {
        return type;
    }

    @Override
    public String getName() {
        return "m";
    }

    @Override
    public int getHostility() {
        return 1;
    }

    @Override
    public String toString() {
        return String.format("Mimiс: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s", getName(), getHealth(), getAgility(), getStrength(), getType());
    }
}
