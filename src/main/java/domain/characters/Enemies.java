package domain.characters;

import domain.battle.CharacterType;
import domain.characters.enemies.EnemiesType;
import domain.navigator.DirectionType;
import domain.navigator.Position;


abstract public class Enemies extends Character {

    private final EnemiesType subType;
    private final int hostility;
    boolean isChasing; // Флаг, устанавливающий, преследует ли монстр игрока
    DirectionType dir; // Направление в котором двигался монстр
    // (используется для змея, который должен постоянно менять направление движения)

    public Enemies(String name, int hostility, int health, int agility, int strength, EnemiesType subType, Position position) {
        super(name, health, agility, strength, position);
        this.hostility = hostility;
        this.subType = subType;
    }

    public int getHostility() {
        return hostility;
    }

    public EnemiesType getSubType() {
        return subType;
    }
}
