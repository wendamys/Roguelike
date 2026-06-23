package domain.characters.enemies;

import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.characters.Enemies;
import domain.navigator.Position;

public class Mimik extends Item {

    public Mimik(String name, ItemsType type, ItemsSubType subType, int value, Position position) {
        super(name, type, subType, value, position);
    }

}
