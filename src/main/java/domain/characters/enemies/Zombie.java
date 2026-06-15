package domain.characters.enemies;

import domain.characters.Enemies;
import domain.navigator.interfaces.PositionInter;

public class Zombie extends Enemies {

    public Zombie(String name, EnemiesType type, int hostility, int health, int agility, int strength, PositionInter positionInter) {
        super(name, type, hostility, health, agility, strength, positionInter);
    }

}
