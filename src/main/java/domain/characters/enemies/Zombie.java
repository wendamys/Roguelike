package domain.characters.enemies;

import domain.battle.CharacterType;
import domain.characters.Enemies;
import domain.navigator.Position;

public class Zombie extends Enemies {

    public Zombie(String name, int hostility, int health, int agility, int strength, EnemiesType subType, Position position) {
        super(name, hostility, health, agility, strength, subType, position);
    }
}
