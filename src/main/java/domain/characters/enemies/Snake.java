package domain.characters.enemies;

import domain.MathUtils.MathUtils;
import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;

public class Snake extends Enemies {

    private int health = 160;
    private int agility = 50;
    private int strength = 30;
    private final EnemiesType type = EnemiesType.SNAKE;

    public Snake(Position position, Level level) {
        super(position);
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
        this.health = randomNumber((int) (health * 0.95), (int) (health * 1.05));
    }

    @Override
    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = randomNumber((int) (agility * 0.95), (int) (agility * 1.05));
    }

    @Override
    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = randomNumber((int) (strength * 0.95), (int) (strength * 1.05));
    }

    @Override
    public String getName() {
        return "s";
    }

    @Override
    public EnemiesType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format(
                "Snake: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s",
                getName(),
                getHealth(),
                getAgility(),
                getStrength(),
                getType()
        );
    }
}
