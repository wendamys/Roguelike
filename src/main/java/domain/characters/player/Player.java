package domain.characters.player;

import domain.characters.Character;
import domain.navigator.Direction;
import domain.navigator.ImmutablePosition;
import domain.navigator.interfaces.Movement;
import domain.navigator.interfaces.Position;

/**
 * класс {@link #Player} описывает поведение игрока
 */
public class Player extends Character implements Movement {

    private final int maxHealth;
    private int gold;

    public Player(String name, int health, int agility, int strength, boolean isAlive, int maxHealth, int gold, Position position) {
        super(name, health, agility, strength, isAlive, position);
        this.maxHealth = maxHealth;
        this.gold = 0;
    }

    public int getMaxHealth() { return maxHealth; }

    public int getGold() { return gold; }

//    /**
//     * метод {@link  #playerMove(Direction)} двигает игрока на одну клетку
//     * @param direction направление движения
//     */
//    public Position playerMove(Direction direction) {
//        Position currentPos = getPosition();
//        Position newPos = currentPos.move(direction, 1);
//        setPosition(newPos);
//        return newPos;
//        // дописать утыкание в стены
//    }

    @Override
    public ImmutablePosition move(Direction direction, int distance) {
        Position currentPos = getPosition();
        ImmutablePosition pos = new ImmutablePosition(currentPos.getX(), currentPos.getY());
        return pos.move(direction, 1);
    }
}