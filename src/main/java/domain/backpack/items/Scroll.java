package domain.backpack.items;

import domain.MathUtils.RandomNumber;
import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.navigator.Position;

public class Scroll extends Item {

    private ItemsSubType subType;

    public Scroll(String name, ItemsType type, ItemsSubType subType, int value, Position position) {
        super("S", ItemsType.SCROLL, subType, value, position);
        this.setSubType();
    }

    /**
     * Задает рандомный подтип предмета
     */
    private void setSubType() {
        RandomNumber randomNumber = new RandomNumber();
        switch (randomNumber.randomNumber(1, 3)) {
            case 1 -> this.subType = ItemsSubType.HEALTH;
            case 2 -> this.subType = ItemsSubType.AGILITY;
            case 3 -> this.subType = ItemsSubType.STRENGTH;
        }
    }

    @Override
    public ItemsSubType getSubType() {
        return subType;
    }

    @Override
    public String toString() {
        return String.format("Scroll: value %d, position(%d, %d), SubType %s",
                getValue(),
                getPosition().getX(),
                getPosition().getY(),
                getSubType()
        );
    }
}
