package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.navigator.Position;

public class Weapon extends Item {
    public Weapon(String name, ItemsType type, int value, Position position) {
        super("W", ItemsType.WEAPON, value, position);
    }
}
