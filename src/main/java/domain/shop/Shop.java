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

    private final ArrayList<Item> elixirList;
    private final ArrayList<Item> foodList;
    private final ArrayList<Item> scrollList;
    private final ArrayList<Item> weaponList;
    private Backpack backpack;
    private Position position;
    private DifficultyType difficulty;

    public Shop(DifficultyType difficulty) {
        this.difficulty = difficulty;
        elixirList = new ArrayList<>(randomNumber(difficulty.getShopMin(), difficulty.getShopMax()));
        foodList = new ArrayList<>(randomNumber(difficulty.getShopMin(), difficulty.getShopMax()));
        scrollList = new ArrayList<>(randomNumber(difficulty.getShopMin(), difficulty.getShopMax()));
        weaponList = new ArrayList<>(randomNumber(difficulty.getShopMin(), difficulty.getShopMax()));

        fillAllList(); // заполнение магазина предметами
    }

    public String getName() {return "$";}

    public ArrayList<Item> getElixirList() {return elixirList;}
    public ArrayList<Item> getFoodList() {return foodList;}
    public ArrayList<Item> getScrollList() {return scrollList;}
    public ArrayList<Item> getWeaponList() {return weaponList;}

    public Backpack getBackpack() {return backpack;}
    public void setBackpack(Backpack backpack) {this.backpack = backpack;}

    public Position getPosition() {return position;}
    public void setPosition(Position position) {this.position = position;}

    public DifficultyType getDifficulty() {return difficulty;}
    public void setDifficulty(DifficultyType difficulty) {this.difficulty = difficulty;}

    private void fillAllList() {
        fillElixirList();
        fillScrollList();
        fillFoodList();
        fillWeaponList();
    }

    private void fillElixirList() {
        for(int i = 0; i < elixirList.size(); i++) {
            elixirList.add(createItem(ItemsType.ELIXIR));
        }
    }

    private void fillScrollList() {
        for(int i = 0; i < scrollList.size(); i++) {
            scrollList.add(createItem(ItemsType.SCROLL));
        }
    }

    private void fillFoodList() {
        for(int i = 0; i < foodList.size(); i++) {
            foodList.add(createItem(ItemsType.FOOD));
        }
    }

    private void fillWeaponList() {
        for(int i = 0; i < weaponList.size(); i++) {
            weaponList.add(createItem(ItemsType.WEAPON));
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
        return (int) Math.max(1, item.getValue() * 0.3);
    }
}
