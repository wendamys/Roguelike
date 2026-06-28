package domain.characters.enemies;

import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;

public class Snake extends Enemies {

    private final EnemiesType type = EnemiesType.SNAKE;
    private int health = 160;
    private int agility = 50;
    private int strength = 30;

    public Snake(Position position) {
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
        return "s";
    }

    @Override
    public int getHostility() {
        return 5;
    }

    @Override
    public String toString() {
        return String.format("Snake: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s", getName(), getHealth(), getAgility(), getStrength(), getType());
    }
}
