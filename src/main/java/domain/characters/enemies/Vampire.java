package domain.characters.enemies;

import domain.characters.Enemies;
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
