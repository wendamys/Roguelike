package datalayer.converter;

import datalayer.dto.ItemsTypeDTO;
import datalayer.dto.ItemsSubTypeDTO;import datalayer.dto.ItemDTO;
import domain.backpack.Item;
import domain.backpack.ItemsSubType;
import domain.backpack.ItemsType;
import domain.backpack.items.Elixir;
import domain.backpack.items.Food;
import domain.backpack.items.Scroll;
import domain.backpack.items.Weapon;

public class ItemConverter {
    public static ItemDTO toDTO(Item item) {
        if (item == null) return null;
        ItemDTO dto = new ItemDTO();
        dto.setType(item.getType() != null ? ItemsTypeDTO.valueOf(item.getType().name()) : null);
        dto.setSubType(item.getSubType() != null ? ItemsSubTypeDTO.valueOf(item.getSubType().name()) : null);
        dto.setValue(item.getValue());
        dto.setPosition(PositionConverter.toDTO(item.getPosition()));
        return dto;
    }

    public static Item fromDTO(ItemDTO dto) {
        if (dto == null) return null;
        ItemsType type = dto.getType() != null ? ItemsType.valueOf(dto.getType().name()) : null;
        ItemsSubType subType = dto.getSubType() != null ? ItemsSubType.valueOf(dto.getSubType().name()) : null;
        
        switch (type) {
            case FOOD:
                Food food = new Food(PositionConverter.fromDTO(dto.getPosition()));
                food.setValue(dto.getValue());
                return food;
            case SCROLL:
                Scroll scroll = new Scroll(PositionConverter.fromDTO(dto.getPosition()));
                scroll.setValue(dto.getValue());
                return scroll;
            case WEAPON:
                Weapon weapon = new Weapon(PositionConverter.fromDTO(dto.getPosition()));
                weapon.setValue(dto.getValue());
                return weapon;
            case ELIXIR:
                Elixir elixir = new Elixir(PositionConverter.fromDTO(dto.getPosition()));
                elixir.setValue(dto.getValue());
                return elixir;
            default:
                return null;
        }
    }
}
