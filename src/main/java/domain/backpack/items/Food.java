package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.navigator.Position;

public class Food extends Item {
    public Food(String name, ItemsType type, int value, Position position) {
        super("F", ItemsType.FOOD, value, position);
    }
}
