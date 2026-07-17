package datalayer.converter;

import datalayer.dto.ItemDTO;
import domain.backpack.Item;
import domain.backpack.items.Elixir;
import domain.backpack.items.Food;
import domain.backpack.items.Scroll;
import domain.backpack.items.Weapon;

public class ItemConverter {

    public static ItemDTO toDTO(Item item) {
        if (item == null) return null;

        ItemDTO dto = new ItemDTO();
        dto.setType(item.getType());
        dto.setName(item.getName());
        dto.setSubtype(item.getSubType());
        dto.setValue(item.getValue());
        dto.setPositionDTO(PositionConverter.toDTO(item.getPosition()));
        return dto;
    }

    public static Item fromDTO(ItemDTO dto) {
        if(dto == null) return null;

        Item item = null;
        switch (dto.getType()) {
            case ELIXIR -> item = new Elixir(PositionConverter.fromDTO(dto.getPositionDTO()));
            case SCROLL -> item = new Scroll(PositionConverter.fromDTO(dto.getPositionDTO()));
            case FOOD -> item = new Food(PositionConverter.fromDTO(dto.getPositionDTO()));
            case WEAPON -> item = new Weapon(PositionConverter.fromDTO(dto.getPositionDTO()));
        }
        item.setName(dto.getName());
        item.setSubType(item.getSubType());
        item.setValueRand(item.getValue());
        return item;
    }
}
