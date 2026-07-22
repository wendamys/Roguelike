package domain.characters.enemies;

import domain.ai.*;
import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

public class Ghost extends Enemies {

    private final EnemiesType type = EnemiesType.GHOST;
    private int health = 80;
    private int agility = 70;
    private int strength = 15;

    public Ghost(Position position) {
        super(position);
        super.setHealthBegin((int) (health * Level.getCoefEnemy()));
        super.setMaxHealth(super.getHealth());
        super.setAgilityRand((int) (agility * Level.getCoefEnemy()));
        super.setStrengthRand((int) (strength * Level.getCoefEnemy()));
    }

    @Override
    public String getName() {
        return "g";
    }

    @Override
    public EnemiesType getType() {
        return type;
    }

    @Override
    public int getHostility() {return 8;}

    @Override
    protected EnemyAI createAI() {
        return new InvisibleAI();
    }
    
    public InvisibleAI getInvisibleAI() {
        return (InvisibleAI) ai;
    }

    @Override
    public String toString() {
        return String.format("Ghost: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s", getName(), getHealth(), getAgility(), getStrength(), getType());
    }
}