package domain.backpack;

import domain.navigator.Position;

public abstract class Item {
    private final String name;
    private final ItemsType type;
    private final ItemsSubType subType;
    private final int value;
    private final Position position;

    public Item(String name, ItemsType type, ItemsSubType subType, int value, Position position) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.position = position;
        this.subType = subType;
    }

    public String getName() { return name; }
    public ItemsType getType() { return type; }
    public int getValue() { return value; }
    protected Position getPosition() { return position; }
    public ItemsSubType getSubType() { return subType; }
}
