package domain.characters;

import domain.characters.enemies.EnemiesType;
import domain.navigator.DirectionType;
import domain.navigator.MovementSystem;
import domain.navigator.Position;


abstract public class Enemies extends Character {

    private final EnemiesType type = null;
    private int hostility = 1;

    public Enemies(Position position) {
        super(position);
    }

    public void setHostility(int hostility) {
        this.hostility = hostility;
    }

    public int getHostility() {
        return hostility;
    }

    public EnemiesType getType() {
        return type;
    }
}
