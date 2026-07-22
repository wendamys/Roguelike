package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;

public class Food extends Item {

    private final ItemsType type = ItemsType.FOOD;
    private final ItemsSubType subType = ItemsSubType.HEALTH;
    private int value = 45;

    public Food(Position position) {
        super(position);
        this.setValueRand((int) (value * Level.getCoefItem()));
    }
    @Override
    public String getName() {
        return "F";
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
        this.value = randomNumber((int) (value * 0.9), (int) (value * 1.1));
    }

    @Override
    public String toString() {
        return String.format("(%d)", getValue());
    }
}