package domain.characters.player;

import domain.characters.Character;
import domain.navigator.DirectionType;
import domain.navigator.Position;

/**
 * класс {@link #Player} описывает поведение игрока
 */
public class Player extends Character {

    private final int maxHealth;
    private int gold;

    public Player(String name, int health, int agility, int strength, int maxHealth, int gold, Position position) {
        super(name, health, agility, strength, position);
        this.maxHealth = maxHealth;
        this.gold = 0;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getGold() {
        return gold;
    }

    public void move(DirectionType direction, int distance) {
        Position currentPos = getPosition();
        Position pos = new Position(currentPos.getX(), currentPos.getY());
        Position newPos = pos.moveDir(direction, 1);
        setPosition(newPos);
    }
}