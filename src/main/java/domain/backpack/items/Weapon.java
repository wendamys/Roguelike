package domain.backpack.items;

import domain.MathUtils.MathUtils;
import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.map.Level;
import domain.navigator.Position;

public class Weapon extends Item {

    private int value = 60;
    private final ItemsType type = ItemsType.WEAPON;
    private final ItemsSubType subType = ItemsSubType.STRENGTH;

    public Weapon(Position position, Level level) {
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
        this.value = random.randomNumber((int) (value * 0.95), (int) (value * 1.05));
    }

    @Override
    public ItemsType getType() {
        return type;
    }

    @Override
    public String getName() {
        return "W";
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