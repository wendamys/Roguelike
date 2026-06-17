package domain.characters.enemies;

import domain.characters.Enemies;
import domain.navigator.Position;

public class Mimik extends Enemies {
    public Mimik(String name, EnemiesType type, int hostility, int health, int agility, int strength, Position position) {
        super(name, type, hostility, health, agility, strength, position);
    }
}
