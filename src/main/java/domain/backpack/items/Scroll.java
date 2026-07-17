package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomItemsSubType;
import static domain.MathUtils.MathUtils.randomNumber;

public class Scroll extends Item {

    private final ItemsType type = ItemsType.SCROLL;
    private ItemsSubType subType;
    private int value = 10;

    public Scroll(Position position) {
        super(position);
        this.setSubTypeRand();
        this.setValueRand((int) (value * Level.getCoefItem()));
    }

    @Override
    public String getName() {
        return "S";
    }

    @Override
    public ItemsType getType() {return type;}

    @Override
    public ItemsSubType getSubType() {return subType;}
    @Override
    public void setSubType(ItemsSubType subType) {this.subType = subType;}
    private void setSubTypeRand() {
        this.subType = randomItemsSubType();
    }

    public void setValueRand(int value) {
        this.value = randomNumber((int) (value * 0.9), (int) (value * 1.1));
    }

    @Override
    public String toString() {
        return String.format("Scroll: value %d, position(%d, %d), SubType %s", getValue(), getPosition().getX(), getPosition().getY(), getSubType());
    }
}