package domain.characters.enemies;

import domain.characters.Enemies;
import domain.characters.Player;
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
        Level level = new Level();
        this.setHealthBegin((int) (health * level.getCoefEnemy()));
        this.setAgility((int) (agility * level.getCoefEnemy()));
        this.setStrength((int) (strength * level.getCoefEnemy()));
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    public void setHealthBegin(int health) {
        this.health = randomNumber((int) (health * 0.97), (int) (health * 1.03));
    }

    @Override
    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = randomNumber((int) (agility * 0.97), (int) (agility * 1.03));
    }

    @Override
    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = randomNumber((int) (strength * 0.97), (int) (strength * 1.03));
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