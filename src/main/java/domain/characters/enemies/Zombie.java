package domain.characters.enemies;

import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;

public class Zombie extends Enemies {

    private final EnemiesType type = EnemiesType.ZOMBIE;
    private int health = 100;
    private int agility = 20;
    private int strength = 20;

    public Zombie(Position position) {
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

    public String getName() {
        return "z";
    }

    @Override
    public int getHostility() {
        return 2;
    }

    @Override
    public String toString() {
        return String.format("Zombie: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s", getName(), getHealth(), getAgility(), getStrength(), getType());
    }
}
