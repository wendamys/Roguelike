package domain.characters.enemies;

import domain.ai.*;
import domain.characters.Enemies;
import domain.gameSession.DifficultyType;
import domain.map.Level;
import domain.navigator.Position;

public class Vampire extends Enemies {

    private final EnemiesType type = EnemiesType.VAMPIRE;
    private int health = 80;
    private int agility = 40;
    private int strength = 40;

    public Vampire(Position position, DifficultyType difficulty) {
        super(position, difficulty);
        super.setHealthBegin((int) (health * difficulty.getCoef() * Level.getCoefEnemy()));
        super.setMaxHealth(super.getHealth());
        super.setAgilityRand((int) (agility * difficulty.getCoef() * Level.getCoefEnemy()));
        super.setStrengthRand((int) (strength * difficulty.getCoef() * Level.getCoefEnemy()));
    }

    @Override
    protected EnemyAI createAI() {
        return new RegenAI();
    }
    
    public RegenAI getRegenAI() {
        return (RegenAI) ai;
    }

    @Override
    public EnemiesType getType() {
        return type;
    }

    @Override
    public String getName() {
        return "v";
    }

    @Override
    public int getHostility() {
        return 6;
    }

    @Override
    public String toString() {
        return String.format("Vampire: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s", getName(), getHealth(), getAgility(), getStrength(), getType());
    }
}
