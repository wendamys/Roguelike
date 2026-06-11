package domain.characters.enemies;

import domain.characters.Enemies;
import domain.navigator.DirectionType;
import domain.navigator.interfaces.Position;

public class Zombie extends Enemies {

    public Zombie(String name, EnemiesType type, int hostility, int health, int agility, int strength, Position position) {
        super(name, type, hostility, health, agility, strength, position);
    }

    @Override
    public DirectionType randomDirection() {
        int randomNumber = random.nextInt(4) + 1;
        System.out.println(randomNumber);
        return switch (randomNumber) {
            case 1 -> DirectionType.FORWARD;
            case 2 -> DirectionType.DOWN;
            case 4 -> DirectionType.RIGHT;
            default -> DirectionType.LEFT;
        };
    }
}
