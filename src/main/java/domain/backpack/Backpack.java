package domain.backpack;

import java.util.ArrayList;
import java.util.Iterator;

public class Backpack {
    private final int maxCapacity = 9;
    private final ArrayList<Item> elixirList = new ArrayList<>(maxCapacity);
    private final ArrayList<Item> foodList = new ArrayList<>(maxCapacity);
    private final ArrayList<Item> scrollList = new ArrayList<>(maxCapacity);
    private final ArrayList<Item> weaponList = new ArrayList<>(maxCapacity);

    /**
     * метод {@link #takeItem(Item)} переносит предмет в лист
     * @param item предмет
     */
    public void takeItem(Item item) {
        switch (item.getType()) {
            case ELIXIR -> addIfPossible(elixirList, item);
            case FOOD -> addIfPossible(foodList, item);
            case SCROLL -> addIfPossible(scrollList, item);
            case WEAPON -> addIfPossible(weaponList, item);
            default -> {}
        }
    }

    /**
     * метод {@link #seeList(ItemsType)} выводит содержимое листа по типу предмета
     * @param type тип предмета
     */
    public void seeList(ItemsType type) {
        switch (type) {
            case ELIXIR -> seeListType(elixirList);
            case FOOD -> seeListType(foodList);
            case SCROLL -> seeListType(scrollList);
            case WEAPON -> seeListType(weaponList);
        }
    }

    public void clearLists() {
        elixirList.clear();
        foodList.clear();
        scrollList.clear();
        weaponList.clear();
    }

    private void seeListType(ArrayList<Item> list) {
        for(var e: list) {
            System.out.println(e);
        }
    }
    private void addIfPossible(ArrayList<Item> list, Item item) {
        if (list.size() < maxCapacity) list.add(item);
    }
}