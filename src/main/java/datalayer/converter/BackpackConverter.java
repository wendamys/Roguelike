package datalayer.converter;

import datalayer.dto.BackpackDTO;
import datalayer.dto.ItemDTO;
import domain.backpack.Backpack;
import domain.backpack.Item;

import java.util.ArrayList;
import java.util.List;

public class BackpackConverter {
    public static BackpackDTO toDTO(Backpack backpack) {
        if (backpack == null) return null;
        BackpackDTO dto = new BackpackDTO();
        
        List<ItemDTO> elixirList = new ArrayList<>();
        for (Item item : backpack.getElixirList()) {
            elixirList.add(ItemConverter.toDTO(item));
        }
        dto.setElixirList(elixirList);
        
        List<ItemDTO> foodList = new ArrayList<>();
        for (Item item : backpack.getFoodList()) {
            foodList.add(ItemConverter.toDTO(item));
        }
        dto.setFoodList(foodList);
        
        List<ItemDTO> scrollList = new ArrayList<>();
        for (Item item : backpack.getScrollList()) {
            scrollList.add(ItemConverter.toDTO(item));
        }
        dto.setScrollList(scrollList);
        
        List<ItemDTO> weaponList = new ArrayList<>();
        for (Item item : backpack.getWeaponList()) {
            weaponList.add(ItemConverter.toDTO(item));
        }
        dto.setWeaponList(weaponList);
        
        return dto;
    }

    public static Backpack fromDTO(BackpackDTO dto) {
        if (dto == null) return null;
        
        Backpack backpack = new Backpack();
        
        if (dto.getElixirList() != null) {
            for (ItemDTO itemDTO : dto.getElixirList()) {
                Item item = ItemConverter.fromDTO(itemDTO);
                if (item != null) backpack.takeItem(item);
            }
        }
        
        if (dto.getFoodList() != null) {
            for (ItemDTO itemDTO : dto.getFoodList()) {
                Item item = ItemConverter.fromDTO(itemDTO);
                if (item != null) backpack.takeItem(item);
            }
        }
        
        if (dto.getScrollList() != null) {
            for (ItemDTO itemDTO : dto.getScrollList()) {
                Item item = ItemConverter.fromDTO(itemDTO);
                if (item != null) backpack.takeItem(item);
            }
        }
        
        if (dto.getWeaponList() != null) {
            for (ItemDTO itemDTO : dto.getWeaponList()) {
                Item item = ItemConverter.fromDTO(itemDTO);
                if (item != null) backpack.takeItem(item);
            }
        }
        
        return backpack;
    }
    
    public static void fromDTO(BackpackDTO dto, Backpack backpack) {
        if (dto == null || backpack == null) return;
        
        if (dto.getElixirList() != null) {
            for (ItemDTO itemDTO : dto.getElixirList()) {
                Item item = ItemConverter.fromDTO(itemDTO);
                if (item != null) backpack.takeItem(item);
            }
        }
        
        if (dto.getFoodList() != null) {
            for (ItemDTO itemDTO : dto.getFoodList()) {
                Item item = ItemConverter.fromDTO(itemDTO);
                if (item != null) backpack.takeItem(item);
            }
        }
        
        if (dto.getScrollList() != null) {
            for (ItemDTO itemDTO : dto.getScrollList()) {
                Item item = ItemConverter.fromDTO(itemDTO);
                if (item != null) backpack.takeItem(item);
            }
        }
        
        if (dto.getWeaponList() != null) {
            for (ItemDTO itemDTO : dto.getWeaponList()) {
                Item item = ItemConverter.fromDTO(itemDTO);
                if (item != null) backpack.takeItem(item);
            }
        }
    }
}
