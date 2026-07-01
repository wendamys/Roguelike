package domain.backpack;

import domain.map.Level;
import domain.navigator.Position;

public abstract class Item {
    private final String name = null;
    private final ItemsType type = null;
    private final ItemsSubType subType = null;
    private final Position position;

    public Item(Position position) {
        this.position = position;
    }

    public String getName() {return name;}

    public ItemsType getType() {return type;}

    public int getValue() { return 30;}

    protected Position getPosition() {return position;}

    public ItemsSubType getSubType() {return subType;}

}