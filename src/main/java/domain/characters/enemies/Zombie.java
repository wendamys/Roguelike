package domain.characters.enemies;

import domain.characters.Enemies;
import domain.navigator.Position;

public class Zombie extends Enemies {

    public Zombie(String name, EnemiesType type, int hostility, int health, int agility, int strength, Position position) {
        super(name, type, hostility, health, agility, strength, position);
    }
}
