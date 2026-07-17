package datalayer.dto;

import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;

public class ItemDTO {
    private ItemsType type;
    private String name;
    private ItemsSubType subtype;
    private int value;
    private PositionDTO positionDTO;

    public ItemDTO() {}

    public ItemsType getType() {return type;}
    public void setType(ItemsType type) {this.type = type; }

    public String getName() {return name; }
    public void setName(String name) {this.name = name; }

    public ItemsSubType getSubtype() {return subtype;}
    public void setSubtype(ItemsSubType subtype) {this.subtype = subtype; }

    public int getValue() {return value;}
    public void setValue(int value) {this.value = value; }

    public PositionDTO getPositionDTO() {return positionDTO;}
    public void setPositionDTO(PositionDTO positionDTO) {this.positionDTO = positionDTO;}
}
