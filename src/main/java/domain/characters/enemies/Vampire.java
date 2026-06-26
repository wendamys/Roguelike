package domain.characters.enemies;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;

public class Vampire extends Enemies {

    private final EnemiesType type = EnemiesType.VAMPIRE;
    private int health = 180;
    private int agility = 30;
    private int strength = 50;

    public Vampire(Position position) {
        super(position);
        Level level = new Level();
        this.setHealth((int) (health * level.getCoefEnemy()));
        this.setAgility((int) (agility * level.getCoefEnemy()));
        this.setStrength((int) (strength * level.getCoefEnemy()));
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = randomNumber((int) (health * 0.96), (int) (health * 1.04));
    }

    @Override
    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = randomNumber((int) (agility * 0.96), (int) (agility * 1.04));
    }

    @Override
    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = randomNumber((int) (strength * 0.96), (int) (strength * 1.04));
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
        return 4;
    }

    @Override
    public String toString() {
        return String.format("Vampire: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s", getName(), getHealth(), getAgility(), getStrength(), getType());
    }
}
