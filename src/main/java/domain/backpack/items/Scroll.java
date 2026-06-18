package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.navigator.Position;

public class Scroll extends Item {
    public Scroll(String name, ItemsType type, ItemsSubType subType, int value, Position position) {
        super("S", ItemsType.SCROLL, subType, value, position);
    }
    @Override
    public String toString() {
        return String.format("Scroll: value %d, position(%d, %d)",
                getValue(),
                getPosition().getX(),
                getPosition().getY()
        );
    }
}
