package domain.characters.enemies;

import domain.characters.Enemies;
import domain.navigator.interfaces.PositionInter;

public class Mimik extends Enemies {
    public Mimik(String name, EnemiesType type, int hostility, int health, int agility, int strength, PositionInter positionInter) {
        super(name, type, hostility, health, agility, strength, positionInter);
    }
}
