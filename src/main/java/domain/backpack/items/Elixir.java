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
    private int value = 50;
    private ItemsSubType subType = null;

    public Elixir(Position position) {
        super(position);
        this.setSubType();
        this.setValue((int) (value * Level.getCoefItem()));
    }

    /**
     * Задает рандомный подтип предмета
     */
    private void setSubType() {
        this.subType = randomItemsSubType();
    }

    @Override
    public ItemsSubType getSubType() {
        return subType;
    }

    @Override
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = randomNumber((int) (value * 0.95), (int) (value * 1.05));
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
    public String toString() {
        return String.format("Elixir: value %d, position(%d, %d), SubType %s", getValue(), getPosition().getX(), getPosition().getY(), getSubType());
    }
}