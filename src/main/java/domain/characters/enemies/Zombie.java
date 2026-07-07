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
        return "z";
    }

    @Override
    public int getHostility() {
        return 2;
    }

    @Override
    public String toString() {
        return String.format("Zombie: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s\nposIsClose: %s",
                getName(),
                getHealth(),
                getAgility(),
                getStrength(),
                getType(),
                getPosition().getIsClose()
        );
    }
}
