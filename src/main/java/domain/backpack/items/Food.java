package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.characters.Player;
import domain.map.Level;
import domain.navigator.Position;

public class Food extends Item {

    private final ItemsSubType subType = ItemsSubType.HEALTH;

    public Food(String name, ItemsType type, ItemsSubType subType, int value, Position position) {
        super("F", ItemsType.FOOD, ItemsSubType.HEALTH, value, position);
    }

    public ItemsSubType getSubType() {
        return subType;
    }

    @Override
    public String toString() {
        return String.format("Food: value %d, position(%d, %d)",
                getValue(),
                getPosition().getX(),
                getPosition().getY()
        );
    }
}