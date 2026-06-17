package domain.backpack;

import domain.navigator.Position;

public abstract class Item {
    private final String name;
    private final ItemsType type;
    private final int value;
    private final Position position;

    public Item(String name, ItemsType type, int value, Position position) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.position = position;
    }

    public String getName() { return name; }
    public ItemsType getType() { return type; }
    public int getValue() { return value; }
    protected Position getPosition() { return position; }

}
