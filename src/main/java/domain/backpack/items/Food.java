package domain.backpack.items;

import domain.MathUtils.MathUtils;
import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.map.Level;
import domain.navigator.Position;

public class Food extends Item {

    private int value = 50;
    private final ItemsType type = ItemsType.FOOD;
    private final ItemsSubType subType = ItemsSubType.HEALTH;

    public Food(Position position, Level level) {
        super(position, level);
        this.setValue((int) (value * level.getCoefItem()));
    }

    static MathUtils random = new MathUtils();

    public ItemsSubType getSubType() {
        return subType;
    }

    @Override
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = random.randomNumber((int) (value * 0.9), (int) (value * 1.1));
    }

    @Override
    public ItemsType getType() {
        return type;
    }

    @Override
    public String getName() {
        return "F";
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