package domain.characters.enemies;

import domain.ai.*;
import domain.characters.Enemies;
import domain.gameSession.DifficultyType;
import domain.map.Level;
import domain.navigator.Position;

public class Ghost extends Enemies {

    private final EnemiesType type = EnemiesType.GHOST;
    private int health = 80;
    private int agility = 70;
    private int strength = 15;

    public Ghost(Position position, DifficultyType difficulty) {
        super(position, difficulty);
        super.setHealthBegin((int) (health * difficulty.getCoef() * Level.getCoefEnemy()));
        super.setMaxHealth(super.getHealth());
        super.setAgilityRand((int) (agility * difficulty.getCoef() * Level.getCoefEnemy()));
        super.setStrengthRand((int) (strength * difficulty.getCoef() * Level.getCoefEnemy()));
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