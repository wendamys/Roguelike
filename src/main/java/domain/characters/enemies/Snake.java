package domain.characters.enemies;

import domain.ai.*;
import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

public class Snake extends Enemies {

    private final EnemiesType type = EnemiesType.SNAKE;
    private int health = 50;
    private int agility = 30;
    private int strength = 20;

    public Snake(Position position) {
        super(position);
        super.setHealthBegin((int) (health * Level.getCoefEnemy()));
        super.setMaxHealth(super.getHealth());
        super.setAgilityRand((int) (agility * Level.getCoefEnemy()));
        super.setStrengthRand((int) (strength * Level.getCoefEnemy()));
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
