package domain.characters.enemies;

import domain.navigator.Position;

public class Snake extends Enemies {

    public Snake(String name, EnemiesType type, int hostility, int health, int agility, int strength, Position position) {
        super(name, type, hostility, health, agility, strength, position);
    }
}
