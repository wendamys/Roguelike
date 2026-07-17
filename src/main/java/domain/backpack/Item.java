package domain.backpack;

import domain.navigator.Position;

public abstract class Item {
    private String name = null;
    private ItemsType type = null;
    private ItemsSubType subType = null;
    private Position position;
    private int value;

    public Item(Position position) {
        this.position = position;
    }

    public String getName() {return name;}
    public void setName(String name) { this.name = name;}

    public ItemsType getType() {return type;}
    public void setType(ItemsType type) { this.type = type;}

    public ItemsSubType getSubType() {return subType;}
    public void setSubType(ItemsSubType subType) { this.subType = subType;}

    public Position getPosition() {return position;}
    public void setPosition(Position position) { this.position = position;}

    public int getValue() { return value; }
    public void setValueRand(int value) { this.value = value; }
}