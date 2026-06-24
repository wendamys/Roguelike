package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomItemsSubType;

public class Elixir extends Item {

    private ItemsSubType subType;

    public Elixir(String name, ItemsType type, ItemsSubType subType, int value, Position position) {
        super("E", ItemsType.ELIXIR, subType, value, position);
        this.setSubType();
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
    public String toString() {
        return String.format("Elixir: value %d, position(%d, %d), SubType %s",
                getValue(),
                getPosition().getX(),
                getPosition().getY(),
                getSubType()
        );
    }
}