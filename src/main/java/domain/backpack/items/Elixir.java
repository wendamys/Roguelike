package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.navigator.Position;

public class Elixir extends Item {

    public Elixir(String name, ItemsType type, ItemsSubType subType, int value, Position position) {
        super("E", ItemsType.ELIXIR, subType, value, position);
    }
    @Override
    public String toString() {
        return String.format("Elixir: value %d, position(%d, %d)",
                getValue(),
                getPosition().getX(),
                getPosition().getY()
                );
    }
}