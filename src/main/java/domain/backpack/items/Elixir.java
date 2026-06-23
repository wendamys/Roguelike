package domain.backpack.items;

import domain.MathUtils.MathUtils;
import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.map.Level;
import domain.navigator.Position;

public class Elixir extends Item {

    private int value = 50;
    private final ItemsType type = ItemsType.ELIXIR;
    private ItemsSubType subType = null;

    public Elixir(Position position, Level level) {
        super(position, level);
        this.setSubType();
        this.setValue((int) (value * level.getCoefItem()));
    }

    static MathUtils random = new MathUtils();

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
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = random.randomNumber((int) (value * 0.95), (int) (value * 1.05));
    }

    @Override
    public String getName() {
        return "E";
    }

    @Override
    public ItemsType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("Elixir: value %d, position(%d, %d), SubType %s",
                getValue(),
                getPosition().getX(),
                getPosition().getY(),
                getSubType()
        );
    }
}