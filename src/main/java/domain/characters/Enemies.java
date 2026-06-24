package domain.characters;

import domain.battle.CharacterType;
import domain.characters.enemies.EnemiesType;
import domain.navigator.DirectionType;
import domain.navigator.Position;


abstract public class Enemies extends Character {

    protected EnemiesType type;
    private int hostility = 1;
    boolean isChasing = false; // Флаг, устанавливающий, преследует ли монстр игрока
    DirectionType dir; // Направление в котором двигался монстр
    // (используется для змея, который должен постоянно менять направление движения)

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
