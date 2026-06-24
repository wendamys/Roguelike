package domain.characters.enemies;

import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;
import domain.MathUtils.*;

public class Ghost extends Enemies {

    private int health = 120;
    private int agility = 50;
    private int strength = 20;
    private final EnemiesType type = EnemiesType.GHOST;

    static MathUtils random = new MathUtils();

    public Ghost(Position position, Level level) {
        super(position);
        this.setHealth((int)(health * level.getCoefEnemy()));
        this.setAgility((int)(agility * level.getCoefEnemy()));
        this.setStrength((int)(strength * level.getCoefEnemy()));
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = random.randomNumber((int) (health * 0.95), (int) (health * 1.05));
    }

    @Override
    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = random.randomNumber((int) (agility * 0.95), (int) (agility * 1.05));
    }

    @Override
    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = random.randomNumber((int) (strength * 0.95), (int) (strength * 1.05));
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
    public String toString() {
        return String.format(
                "Ghost: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s",
                getName(),
                getHealth(),
                getAgility(),
                getStrength(),
                getType()
        );
    }
}
