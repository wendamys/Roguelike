package domain.backpack;

import domain.characters.Player;

import java.util.ArrayList;


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
            case WEAPON -> {
                if(weaponList.size() < 9) addIfPossible(weaponList, item);
                else {
                    weaponList.remove(9);
                    weaponList.add(item);
                }
            }
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

    /**
     * метод {@link #clearLists()} чистит все предметы в рюкзаке
     */
    public void clearLists() {
        elixirList.clear();
        foodList.clear();
        scrollList.clear();
        weaponList.clear();
    }

//    /**
//     * метод {@link #useItem(Item, Player)} использует предмет и удаляет его из рюкзака
//     * @param item используемый предмет
//     * @param player игрок
//     */
//    public void useItem(Item item, Player player) {
//        player.useItemValue(item);
//        removeItem(item);
//    }

    public void useItemFood(int numItem, Player player) {
        player.useItemValue(foodList.get(numItem));
        foodList.remove(numItem);
    }
    public void useItemScroll(int numItem, Player player) {
        player.useItemValue(scrollList.get(numItem));
        scrollList.remove(numItem);
    }
    public void useItemElixir(int numItem, Player player) {
        player.useItemValue(elixirList.get(numItem));
        elixirList.remove(numItem);
    }
    public void useItemWeapon(int numItem, Player player) {
        player.useItemValue(weaponList.get(numItem));
    }

    /**
     * метод {@link #removeItem(Item)} удаляет предмет из рюкзака
     * @param item предмет
     */
    public void removeItem(Item item) {
        switch (item.getType()) {
            case ELIXIR -> elixirList.remove(item);
            case FOOD -> foodList.remove(item);
            case SCROLL -> scrollList.remove(item);
            case WEAPON -> weaponList.remove(item);
            default -> {}
        }
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