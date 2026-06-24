package domain.backpack.items;

import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomItemsSubType;
import static domain.MathUtils.MathUtils.randomNumber;

public class Scroll extends Item {

    private final ItemsType type = ItemsType.SCROLL;
    private int value = 10;
    private ItemsSubType subType = null;

    public Scroll(Position position, Level level) {
        super(position, level);
        this.setSubType();
        this.setValue((int) (value * level.getCoefItem()));
    }

    /**
     * Задает рандомный подтип предмета
     */
    private void setSubType() {
        this.subType = randomItemsSubType();
    }

    @Override
    public ItemsSubType getSubType() {
        return subType;
    }

    @Override
    public String getName() {
        return "S";
    }

    @Override
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = randomNumber((int) (value * 0.9), (int) (value * 1.1));
    }

    @Override
    public ItemsType getType() {
        return type;
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
