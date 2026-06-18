package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.navigator.Position;

public class Weapon extends Item {

    private final ItemsSubType subType = ItemsSubType.STRENGTH;

    public Weapon(String name, ItemsType type, ItemsSubType subType, int value, Position position) {
        super("W", ItemsType.WEAPON, ItemsSubType.STRENGTH, value, position);
    }

    public ItemsSubType getSubType() {
        return subType;
    }

    @Override
    public String toString() {
        return String.format("Weapon: value %d, position(%d, %d)",
                getValue(),
                getPosition().getX(),
                getPosition().getY()
        );
    }
}
