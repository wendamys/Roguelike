package domain.backpack;

import domain.characters.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Backpack {
    private final int maxCapacity = 9;
    private final ArrayList<Item> elixirList = new ArrayList<>(maxCapacity);
    private final ArrayList<Item> foodList = new ArrayList<>(maxCapacity);
    private final ArrayList<Item> scrollList = new ArrayList<>(maxCapacity);
    private final ArrayList<Item> weaponList = new ArrayList<>(maxCapacity);

    private boolean isBuyShop = false;

    /**
     * метод переносит предмет в лист
     * @param item предмет
     */
    public void takeItem(Item item) {
        switch (item.getType()) {
            case ELIXIR -> addIfPossible(elixirList, item);
            case FOOD -> addIfPossible(foodList, item);
            case SCROLL -> addIfPossible(scrollList, item);
            case WEAPON -> addIfPossible(weaponList, item);
        }
    }

    /**
     * метод выводит содержимое листа по типу предмета
     * @param type тип предмета
     */
    public void seeList(ItemsType type) {
        switch (type) {
            case ELIXIR -> seeListType(elixirList, "Эликсиры");
            case FOOD -> seeListType(foodList, "Еда");
            case SCROLL -> seeListType(scrollList, "Свитки");
            case WEAPON -> seeListType(weaponList, "Оружие");
        }
    }

    /**
     * метод выводит содержимое листа с индексами
     * @param list список предметов
     * @param title заголовок списка
     */
    private void seeListType(ArrayList<Item> list, String title) {
        System.out.println("\n" + title);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + list.get(i));
        }
    }

    /**
     * Получает предмет по индексу
     * @param index индекс предмета
     * @param type тип предмета
     * @return предмет или null
     */
    public Item getItem(int index, ItemsType type) {
        switch (type) {
            case ELIXIR -> {
                if (index >= 0 && index < elixirList.size()) return elixirList.get(index);
            }
            case FOOD -> {
                if (index >= 0 && index < foodList.size()) return foodList.get(index);
            }
            case SCROLL -> {
                if (index >= 0 && index < scrollList.size()) return scrollList.get(index);
            }
            case WEAPON -> {
                if (index >= 0 && index < weaponList.size()) return weaponList.get(index);
            }
        }
        return null;
    }

    /**
     * Использует предмет по индексу
     * @param index индекс предмета
     * @param type тип предмета
     * @param player игрок
     * @return true если предмет успешно использован, false иначе
     */
    public boolean useItemByIndex(int index, ItemsType type, Player player) {
        if (index < 0) return false;
        
        Item item = getItem(index, type);
        if (item == null) return false;
        
        switch (type) {
            case ELIXIR -> useItemElixir(index, player);
            case FOOD -> useItemFood(index, player);
            case SCROLL -> useItemScroll(index, player);
            case WEAPON -> useItemWeapon(index, player);
        }
        return true;
    }

    /**
     * Использует еду по индексу
     * @param numItem индекс предмета
     * @param player игрок
     * @return true если еда использована, false если индекс вне диапазона
     */
    public boolean useItemFood(int numItem, Player player) {
        if (numItem < 0 || numItem >= foodList.size()) return false;
        player.useItemValue(foodList.get(numItem));
        foodList.remove(numItem);
        return true;
    }

    /**
     * Использует свиток по индексу
     * @param numItem индекс предмета
     * @param player игрок
     * @return true если свиток использован, false если индекс вне диапазона
     */
    public boolean useItemScroll(int numItem, Player player) {
        if (numItem < 0 || numItem >= scrollList.size()) return false;
        player.useItemValue(scrollList.get(numItem));
        scrollList.remove(numItem);
        return true;
    }

    /**
     * Использует эликсир по индексу
     * @param numItem индекс предмета
     * @param player игрок
     * @return true если эликсир использован, false если индекс вне диапазона
     */
    public boolean useItemElixir(int numItem, Player player) {
        if (numItem < 0 || numItem >= elixirList.size()) return false;
        player.useItemValue(elixirList.get(numItem));
        elixirList.remove(numItem);
        return true;
    }

    /**
     * Использует оружие по индексу
     * @param numItem индекс предмета
     * @param player игрок
     * @return true если оружие использовано, false если индекс вне диапазона
     */
    public boolean useItemWeapon(int numItem, Player player) {
        if (numItem < 0 || numItem >= weaponList.size()) return false;
        player.useItemValue(weaponList.get(numItem));
        weaponList.remove(numItem);
        return true;
    }
    
    private void addIfPossible(ArrayList<Item> list, Item item) {
        if (list.size() < maxCapacity) {
            list.add(item);
            item.setPosition(null);
        }
    }

    /**
     * Получает силу оружия из рюкзака
     * @return сила оружия (сумма значений всех оружий)
     */
    public int getWeaponPower() {
        int power = 0;
        for (Item item : weaponList) {
            power += item.getValue();
        }
        return power;
    }

    /**
     * Очищает все списки предметов
     */
    public void clearLists() {
        elixirList.clear();
        foodList.clear();
        scrollList.clear();
        weaponList.clear();
    }

    /**
     * Получает неизменяемый список эликтиров
     * @return неизменяемый список эликтиров
     */
    public List<Item> getElixirList() { 
        return Collections.unmodifiableList(elixirList); 
    }
    
    /**
     * Получает неизменяемый список еды
     * @return неизменяемый список еды
     */
    public List<Item> getFoodList() { 
        return Collections.unmodifiableList(foodList); 
    }
    
    /**
     * Получает неизменяемый список свитков
     * @return неизменяемый список свитков
     */
    public List<Item> getScrollList() { 
        return Collections.unmodifiableList(scrollList); 
    }
    
    /**
     * Получает неизменяемый список оружия
     * @return неизменяемый список оружия
     */
    public List<Item> getWeaponList() { 
        return Collections.unmodifiableList(weaponList); 
    }
}
