package datalayer.converter;

import datalayer.dto.BackpackDTO;
import domain.backpack.Backpack;


public class BackpackConverter {

    public static BackpackDTO toDTO(Backpack backpack) {
        if (backpack == null) return null;

        BackpackDTO dto = new BackpackDTO();
        backpack.getElixirList().forEach(item -> dto.getElixirListDTO().add(ItemConverter.toDTO(item)));
        backpack.getScrollList().forEach(item -> dto.getScrollListDTO().add(ItemConverter.toDTO(item)));
        backpack.getFoodList().forEach(item -> dto.getFoodListDTO().add(ItemConverter.toDTO(item)));
        backpack.getWeaponList().forEach(item -> dto.getWeaponListDTO().add(ItemConverter.toDTO(item)));
        return dto;
        }

    public static Backpack fromDTO(BackpackDTO dto) {
        if (dto == null) return null;

        Backpack backpack = new Backpack();
        dto.getElixirListDTO().forEach(itemDTO -> backpack.takeItem(ItemConverter.fromDTO(itemDTO)));
        dto.getScrollListDTO().forEach(itemDTO -> backpack.takeItem(ItemConverter.fromDTO(itemDTO)));
        dto.getFoodListDTO().forEach(itemDTO -> backpack.takeItem(ItemConverter.fromDTO(itemDTO)));
        dto.getWeaponListDTO().forEach(itemDTO -> backpack.takeItem(ItemConverter.fromDTO(itemDTO)));
        return backpack;
    }
}