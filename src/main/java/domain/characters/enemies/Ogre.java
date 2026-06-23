package domain.characters.enemies;

import domain.MathUtils.MathUtils;
import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

public class Ogre extends Enemies {

    private int health = 200;
    private int agility = 50;
    private int strength = 50;
    private final EnemiesType type = EnemiesType.OGRE;

    public Ogre(Position position, Level level) {
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
        this.health = random.randomNumber((int) (health * 0.97), (int) (health * 1.03));
    }

    @Override
    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = random.randomNumber((int) (agility * 0.97), (int) (agility * 1.03));
    }

    @Override
    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = random.randomNumber((int) (strength * 0.97), (int) (strength * 1.03));
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
    public String toString() {
        return String.format(
                "Ogre: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s",
                getName(),
                getHealth(),
                getAgility(),
                getStrength(),
                getType()
        );
    }
}