package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.navigator.Position;

public class Elixir extends Item {
    public Elixir(String name, ItemsType type, int value, Position position) {
        super("E", ItemsType.ELIXIR, value, position);
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
