package domain.characters.enemies;

import domain.ai.*;
import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

public class Ogre extends Enemies {

    private final EnemiesType type = EnemiesType.OGRE;
    private int health = 80;
    private int agility = 25;
    private int strength = 40;

    public Ogre(Position position) {
        super(position);
        super.setHealthBegin((int) (health * Level.getCoefEnemy()));
        super.setMaxHealth(super.getHealth());
        super.setAgilityRand((int) (agility * Level.getCoefEnemy()));
        super.setStrengthRand((int) (strength * Level.getCoefEnemy()));
    }

    @Override
    protected EnemyAI createAI() {
        return new StunAI();
    }
    
    public StunAI getStunAI() {
        return (StunAI) ai;
    }

    @Override
    public EnemiesType getType() {
        return type;
    }

    @Override
    public String getName() {
        return "o";
    }

    @Override
    public int getHostility() {
        return 6;
    }

    @Override
    public String toString() {
        return String.format("Ogre: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s", getName(), getHealth(), getAgility(), getStrength(), getType());
    }
}
