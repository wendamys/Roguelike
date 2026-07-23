package domain.characters.enemies;

import domain.ai.*;
import domain.characters.Enemies;
import domain.gameSession.DifficultyType;
import domain.map.Level;
import domain.navigator.Position;

public class Snake extends Enemies {

    private final EnemiesType type = EnemiesType.SNAKE;
    private int health = 60;
    private int agility = 50;
    private int strength = 20;

    public Snake(Position position, DifficultyType difficulty) {
        super(position, difficulty);
        super.setHealthBegin((int) (health * difficulty.getCoef() * Level.getCoefEnemy()));
        super.setMaxHealth(super.getHealth());
        super.setAgilityRand((int) (agility * difficulty.getCoef() * Level.getCoefEnemy()));
        super.setStrengthRand((int) (strength * difficulty.getCoef() * Level.getCoefEnemy()));
    }

    @Override
    protected EnemyAI createAI() {
        return new DebuffAI();
    }
    
    public DebuffAI getDebuffAI() {
        return (DebuffAI) ai;
    }

    @Override
    public EnemiesType getType() {
        return type;
    }

    @Override
    public String getName() {
        return "s";
    }

    @Override
    public int getHostility() {
        return 5;
    }

    @Override
    public String toString() {
        return String.format("Snake: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s", getName(), getHealth(), getAgility(), getStrength(), getType());
    }
}
