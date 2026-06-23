package domain.characters.enemies;

import domain.MathUtils.MathUtils;
import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

public class Zombie extends Enemies {

    private int health = 100;
    private int agility = 20;
    private int strength = 20;
    private final EnemiesType type = EnemiesType.ZOMBIE;

    public Zombie(Position position, Level level) {
        super(position);
        this.setHealth((int)(health * level.getCoefEnemy()));
        this.setAgility((int)(agility * level.getCoefEnemy()));
        this.setStrength((int)(strength * level.getCoefEnemy()));
    }

    static MathUtils random = new MathUtils();

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = random.randomNumber((int) (health * 0.9), (int) (health * 1.1));
    }

    @Override
    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = random.randomNumber((int) (agility * 0.9), (int) (agility * 1.1));
    }

    @Override
    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = random.randomNumber((int) (strength * 0.9), (int) (strength * 1.1));
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
    public String toString() {
        return String.format(
                "Zombie: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s",
                getName(),
                getHealth(),
                getAgility(),
                getStrength(),
                getType()
        );
    }
}
