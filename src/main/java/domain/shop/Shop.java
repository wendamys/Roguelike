package domain.shop;

import domain.backpack.Backpack;
import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.backpack.items.Elixir;
import domain.backpack.items.Food;
import domain.backpack.items.Scroll;
import domain.backpack.items.Weapon;
import domain.gameSession.DifficultyType;
import domain.navigator.Position;

import java.util.ArrayList;

import static domain.MathUtils.MathUtils.randomNumber;

/**
 * Магазин уровня: набор предметов, доступных к покупке.
 * Количество предметов каждого типа зависит от уровня сложности.
 * Покупка пока не реализована - класс отдаёт только ассортимент и цены.
 */
public class Shop {

    public static final double PRICE_COEFFICIENT = 0.3;

    private final ArrayList<Item> elixirList;
    private final ArrayList<Item> foodList;
    private final ArrayList<Item> scrollList;
    private final ArrayList<Item> weaponList;
    private Backpack backpack;
    private Position position;
    private final DifficultyType difficulty;

    public Shop(DifficultyType difficulty) {
        this.difficulty = difficulty;
        int elixirCount = randomNumber(difficulty.getShopMin(), difficulty.getShopMax());
        int foodCount = randomNumber(difficulty.getShopMin(), difficulty.getShopMax());
        int scrollCount = randomNumber(difficulty.getShopMin(), difficulty.getShopMax());
        int weaponCount = randomNumber(difficulty.getShopMin(), difficulty.getShopMax());

        elixirList = new ArrayList<>();
        foodList = new ArrayList<>();
        scrollList = new ArrayList<>();
        weaponList = new ArrayList<>();

        // Заполняем списки нужным количеством элементов
        fillList(elixirList, ItemsType.ELIXIR, elixirCount);
        fillList(foodList, ItemsType.FOOD, foodCount);
        fillList(scrollList, ItemsType.SCROLL, scrollCount);
        fillList(weaponList, ItemsType.WEAPON, weaponCount);
    }

    public String getName() {return "$";}

    public ArrayList<Item> getElixirList() {return new ArrayList<>(elixirList);}
    public ArrayList<Item> getFoodList() {return new ArrayList<>(foodList);}
    public ArrayList<Item> getScrollList() {return new ArrayList<>(scrollList);}
    public ArrayList<Item> getWeaponList() {return new ArrayList<>(weaponList);}

    public Backpack getBackpack() {return backpack;}
    public void setBackpack(Backpack backpack) {this.backpack = backpack;}

    public Position getPosition() {return position;}
    public void setPosition(Position position) {this.position = position;}

    public DifficultyType getDifficulty() {return difficulty;}

    private void fillList(ArrayList<Item> list, ItemsType type, int count) {
        for(int i = 0; i < count; i++) {
            list.add(createItem(type));
        }
    }

    /**
     * метод создаёт предмет нужного типа вне карты
     * @param type тип предмета
     * @return предмет без позиции
     */
    private Item createItem(ItemsType type) {
        return switch (type) {
            case ELIXIR -> new Elixir(null);
            case FOOD -> new Food(null);
            case SCROLL -> new Scroll(null);
            case WEAPON -> new Weapon(null);
        };
    }

    /**
     * метод считает цену предмета
     * @param item предмет
     * @return цена в золоте
     */
    public int priceOf(Item item) {
        return (int) Math.max(1, item.getValue() * PRICE_COEFFICIENT);
    }
}
