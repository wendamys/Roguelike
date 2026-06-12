package domain.characters.enemies;

import domain.characters.Enemies;
import domain.navigator.interfaces.Position;

public class Ghost extends Enemies {
    public Ghost(String name, EnemiesType type, int hostility, int health, int agility, int strength, Position position) {
        super(name, type, hostility, health, agility, strength, position);
    }
}
