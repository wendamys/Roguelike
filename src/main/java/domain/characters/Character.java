package domain.characters;

import domain.navigator.Position;

/**
 * Абстрактный класс описывает главные характеристики персонажей в игре
 */
public abstract class Character {
    private Position position;

    public Character(Position position) {
        this.position = position;
    }

    public abstract String getName();

    public abstract int getAgility();

    public abstract int getHealth();
    public abstract void setHealth(int health);

    public abstract int getStrength();

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
}
