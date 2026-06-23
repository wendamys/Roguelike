package domain.characters.enemies;

import domain.MathUtils.MathUtils;
import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

public class Mimik extends Enemies {

    private int health = 300;
    private int agility = 100;
    private int strength = 10;
    private final EnemiesType type = EnemiesType.MIMIK;

    public Mimik(Position position, Level level) {
        super(position);
        this.setHealth((int)(health * level.getCoefEnemy()));
        this.setAgility((int)(agility * level.getCoefEnemy()));
        this.setStrength((int)(strength * level.getCoefEnemy()));
    }

    static MathUtils random = new MathUtils();

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = random.randomNumber((int) (health * 0.98), (int) (health * 1.02));
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = random.randomNumber((int) (agility * 0.98), (int) (agility * 1.02));
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = random.randomNumber((int) (strength * 0.98), (int) (strength * 1.02));
    }

    public EnemiesType getType() {
        return type;
    }

    public String getName() {
        return "m";
    }

    @Override
    public String toString() {
        return String.format(
                "Mimik: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s",
                getName(),
                getHealth(),
                getAgility(),
                getStrength(),
                getType()
        );
    }
}
