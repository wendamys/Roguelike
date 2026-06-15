package domain.characters.player;

import domain.characters.Character;
import domain.navigator.DirectionType;
import domain.navigator.ImmutablePositionInter;
import domain.navigator.interfaces.Movement;
import domain.navigator.interfaces.PositionInter;

/**
 * класс {@link #Player} описывает поведение игрока
 */
public class Player extends Character implements Movement {

    private final int maxHealth;
    private int gold;

    public Player(String name, int health, int agility, int strength, int maxHealth, int gold, PositionInter positionInter) {
        super(name, health, agility, strength, positionInter);
        this.maxHealth = maxHealth;
        this.gold = 0;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getGold() {
        return gold;
    }

    @Override
    public void move(DirectionType direction, int distance) {
        PositionInter currentPos = getPosition();
        ImmutablePositionInter pos = new ImmutablePositionInter(currentPos.getX(), currentPos.getY());
        ImmutablePositionInter newPos = pos.moveDir(direction, 1);
        setPosition(newPos);
    }
}