package domain.characters.enemies;

import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;

public class Ogre extends Enemies {

    private final EnemiesType type = EnemiesType.OGRE;
    private int health = 200;
    private int agility = 50;
    private int strength = 50;

    public Ogre(Position position) {
        super(position);
        super.setHealthBegin((int) (health * Level.getCoefEnemy()));
        super.setAgility((int) (agility * Level.getCoefEnemy()));
        super.setStrength((int) (strength * Level.getCoefEnemy()));
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