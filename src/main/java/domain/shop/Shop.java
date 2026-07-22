package domain.shop;

import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.backpack.items.Elixir;
import domain.backpack.items.Food;
import domain.backpack.items.Scroll;
import domain.backpack.items.Weapon;
import domain.gameSession.DifficultyType;

import java.util.ArrayList;
import java.util.List;

import static domain.MathUtils.MathUtils.randomNumber;

/**
 * Магазин уровня: набор предметов, доступных к покупке.
 * Количество предметов каждого типа зависит от уровня сложности.
 * Покупка пока не реализована - класс отдаёт только ассортимент и цены.
 */
public class Shop {

    private final List<Item> items = new ArrayList<>();

    public Shop(DifficultyType difficulty) {
        for (ItemsType type : ItemsType.values()) {
            int count = randomNumber(difficulty.getShopMin(), difficulty.getShopMax());
            for (int i = 0; i < count; i++) {
                items.add(createItem(type));
            }
        }
    }

    public List<Item> getItems() {
        return items;
    }

    /**
     * метод считает цену предмета
     * @param item предмет
     * @return цена в золоте
     */
    public int priceOf(Item item) {
        return Math.max(1, item.getValue() * 2);
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
}
