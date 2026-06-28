package domain.characters.enemies;

import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;

public class Mimic extends Enemies {

    private final EnemiesType type = EnemiesType.MIMIC;
    private int health = 300;
    private int agility = 100;
    private int strength = 10;

    public Mimic(Position position) {
        super(position);
        Level level = new Level();
        super.setAgility((int) (agility * level.getCoefEnemy()));
        super.setHealthBegin((int) (health * level.getCoefEnemy()));
        super.setStrength((int) (strength * level.getCoefEnemy()));
    }

    @Override
    public EnemiesType getType() {
        return type;
    }

    @Override
    public String getName() {
        return "m";
    }

    @Override
    public int getHostility() {
        return 1;
    }

    @Override
    public String toString() {
        return String.format("Mimiс: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s", getName(), getHealth(), getAgility(), getStrength(), getType());
    }
}
