package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.navigator.Position;

public class Scroll extends Item {
    public Scroll(String name, ItemsType type, int value, Position position) {
        super("S", ItemsType.SCROLL, value, position);
    }
}
