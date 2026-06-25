package domain.characters;

import domain.characters.enemies.EnemiesType;
import domain.navigator.DirectionType;
import domain.navigator.Position;


abstract public class Enemies extends Character {

    protected EnemiesType type;
    boolean isChasing = false; // Флаг, устанавливающий, преследует ли монстр игрока
    DirectionType dir; // Направление в котором двигался монстр
    private int hostility;
    // (используется для змея, который должен постоянно менять направление движения)

    public Enemies(Position position) {
        super(position);
    }

    public int getHostility() {
        return hostility;
    }

    public void setHostility(int hostility) {
        this.hostility = hostility;
    }

    public EnemiesType getType() {
        return type;
    }

    public boolean isHostility(Player player) {
        return false;
    }
}