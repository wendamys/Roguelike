package datalayer.dto;

public class ItemDTO {
    private ItemsTypeDTO type;
    private ItemsSubTypeDTO subType;
    private int value;
    private PositionDTO position;

    public ItemDTO() {}

    public ItemsTypeDTO getType() { return type; }
    public void setType(ItemsTypeDTO type) { this.type = type; }

    public ItemsSubTypeDTO getSubType() { return subType; }
    public void setSubType(ItemsSubTypeDTO subType) { this.subType = subType; }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public PositionDTO getPosition() { return position; }
    public void setPosition(PositionDTO position) { this.position = position; }
}
