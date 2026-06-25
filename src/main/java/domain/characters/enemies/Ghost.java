package domain.characters.enemies;

import domain.MathUtils.MathUtils;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;

public class Ghost extends Enemies {

    private final EnemiesType type = EnemiesType.GHOST;
    private int health = 120;
    private int agility = 50;
    private int strength = 20;

    public Ghost(Position position, Level level) {
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
    public EnemiesType getType() {
        return type;
    }

    @Override
    public String getName() {
        return "g";
    }

    @Override
    public int getHostility() {
        return 3;
    }

    @Override
    public boolean isHostility(Player player) {
        return getPosition().distanceTo(player.getPosition()) <= getHostility();
    }

    @Override
    public String toString() {
        return String.format("Ghost: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s", getName(), getHealth(), getAgility(), getStrength(), getType());
    }
}
