package domain.backpack.items;

import domain.MathUtils.MathUtils;
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
        MathUtils randomNumber = new MathUtils();
        this.subType = randomNumber.randomType();
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
