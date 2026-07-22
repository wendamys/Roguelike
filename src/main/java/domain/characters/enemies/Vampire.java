package domain.characters.enemies;

import domain.ai.*;
import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

public class Vampire extends Enemies {

    private final EnemiesType type = EnemiesType.VAMPIRE;
    private int health = 80;
    private int agility = 40;
    private int strength = 40;

    public Vampire(Position position) {
        super(position);
        super.setHealthBegin((int) (health * Level.getCoefEnemy()));
        super.setMaxHealth(super.getHealth());
        super.setAgilityRand((int) (agility * Level.getCoefEnemy()));
        super.setStrengthRand((int) (strength * Level.getCoefEnemy()));
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
