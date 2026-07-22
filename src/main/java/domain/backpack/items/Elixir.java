package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomItemsSubType;
import static domain.MathUtils.MathUtils.randomNumber;

public class Elixir extends Item {

    private final ItemsType type = ItemsType.ELIXIR;
    private ItemsSubType subType;
    private int value = 30;

    public Elixir(Position position) {
        super(position);
        this.setSubTypeRand();
        this.setValueRand((int) (value * Level.getCoefItem()));
    }

    @Override
    public String getName() {
        return "E";
    }

    @Override
    public ItemsType getType() {
        return type;
    }

    @Override
    public ItemsSubType getSubType() {return subType;}
    @Override
    public void setSubType(ItemsSubType subType) {this.subType = subType;}
    private void setSubTypeRand() {
        this.subType = randomItemsSubType();
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
        return String.format("Elixir: value %d, subType %s", getValue(), getSubType());
    }
}