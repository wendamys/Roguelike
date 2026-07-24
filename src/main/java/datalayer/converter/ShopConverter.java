package datalayer.converter;

import datalayer.dto.ItemDTO;
import datalayer.dto.ShopDTO;
import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.backpack.items.*;
import domain.gameSession.DifficultyType;
import domain.shop.Shop;

import java.util.ArrayList;

public class ShopConverter {

    public static ShopDTO toDTO(Shop shop) {
        if (shop == null) return null;

        ShopDTO dto = new ShopDTO();
        dto.setDifficulty(shop.getDifficulty().name());

        // Конвертируем списки предметов
        convertItemList(shop.getElixirList(), dto.getElixirList());
        convertItemList(shop.getFoodList(), dto.getFoodList());
        convertItemList(shop.getScrollList(), dto.getScrollList());
        convertItemList(shop.getWeaponList(), dto.getWeaponList());

        return dto;
    }

    public static Shop fromDTO(ShopDTO dto) {
        if (dto == null) return null;

        DifficultyType difficulty = DifficultyType.valueOf(dto.getDifficulty());
        Shop shop = new Shop(difficulty);

        // Очищаем стандартные списки и заполняем сохраненными
        shop.getElixirList().clear();
        shop.getFoodList().clear();
        shop.getScrollList().clear();
        shop.getWeaponList().clear();

        fromItemList(dto.getElixirList(), shop.getElixirList());
        fromItemList(dto.getFoodList(), shop.getFoodList());
        fromItemList(dto.getScrollList(), shop.getScrollList());
        fromItemList(dto.getWeaponList(), shop.getWeaponList());

        return shop;
    }

    private static void convertItemList(ArrayList<Item> from, ArrayList<ItemDTO> to) {
        to.clear();
        for (Item item : from) {
            to.add(ItemConverter.toDTO(item));
        }
    }

    private static void fromItemList(ArrayList<ItemDTO> from, ArrayList<Item> to) {
        for (ItemDTO dto : from) {
            Item item = ItemConverter.fromDTO(dto);
            if (item != null) {
                to.add(item);
            }
        }
    }
}
