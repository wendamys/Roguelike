package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;

public class Weapon extends Item {

    private final ItemsType type = ItemsType.WEAPON;
    private final ItemsSubType subType = ItemsSubType.STRENGTH;
    private int value = 60;

    public Weapon(Position position) {
        super(position);
        this.setValueRand((int) (value * Level.getCoefItem()));
    }

    @Override
    public String getName() {
        return "W";
    }

    @Override
    public ItemsType getType() {
        return type;
    }

    public ItemsSubType getSubType() {
        return subType;
    }

    @Override
    public int getValue() {
        return value;
    }
    public void setValue(int value) {this.value = value;}
    public void setValueRand(int value) {
        this.value = randomNumber((int) (value * 0.95), (int) (value * 1.05));
    }

    @Override
    public String toString() {
        return String.format("Weapon: value %d, position(%d, %d)", getValue(), getPosition().getX(), getPosition().getY());
    }
}