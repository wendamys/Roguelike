package domain.characters.enemies;

import domain.ai.*;
import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;

public class Ghost extends Enemies {

    private final EnemiesType type = EnemiesType.GHOST;
    private int health = 70;
    private int agility = 40;
    private int strength = 20;

    public Ghost(Position position) {
        super(position);
        super.setHealthBegin((int) (health * Level.getCoefEnemy()));
        super.setMaxHealth(super.getHealth());
        super.setAgility((int) (agility * Level.getCoefEnemy()));
        super.setStrength((int) (strength * Level.getCoefEnemy()));
    }

    @Override
    protected EnemyAI createAI() {
        return new InvisibleAI();
    }
    
    public InvisibleAI getInvisibleAI() {
        return (InvisibleAI) ai;
    }

    @Override
    public EnemiesType getType() {
        return type;
    }

    @Override
    public String getName() {
        return "g";
    }

    @Override
    public int getHostility() {
        return 5;
    }

    @Override
    public String toString() {
        return String.format("Ghost: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s", getName(), getHealth(), getAgility(), getStrength(), getType());
    }
}
