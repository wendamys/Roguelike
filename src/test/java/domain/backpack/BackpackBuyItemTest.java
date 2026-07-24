package domain.backpack;

import domain.characters.Player;
import domain.gameSession.DifficultyType;
import domain.navigator.Position;
import domain.shop.Shop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест для проверки покупки предметов из магазина
 */
class BackpackBuyItemTest {

    private Backpack backpack;
    private Player player;
    private Shop shop;

    @BeforeEach
    void setUp() {
        backpack = new Backpack();
        player = new Player(new Position(0, 0));
        player.setGold(1000); // Даем много золота
        shop = new Shop(DifficultyType.EASY);
    }

    @Test
    void testBuyElixirFromShop() {
        // Проверяем, что в магазине есть эликсиры
        int initialShopElixirs = shop.getElixirList().size();
        assertTrue(initialShopElixirs > 0, "В магазине должны быть эликсиры для теста");

        // Проверяем, что в рюкзаке нет эликсиров
        int initialBackpackElixirs = backpack.getElixirList().size();
        assertEquals(0, initialBackpackElixirs, "В рюкзаке не должно быть эликсиров перед покупкой");

        // Покупаем эликсир
        Item elixir = shop.getElixirList().getFirst();
        int price = shop.priceOf(elixir);
        assertTrue(player.getGold() >= price, "У игрока должно хватить золота");

        // Имитируем покупку
        backpack.takeItem(elixir);
        player.setGold(player.getGold() - price);
        shop.removeElixir();

        // Проверяем, что эликсир добавлен в рюкзак
        assertEquals(1, backpack.getElixirList().size(), "В рюкзаке должен быть 1 эликсир после покупки");

        // Проверяем, что золото списано
        assertEquals(1000 - price, player.getGold(), "Золото должно быть списано");

        // Проверяем, что в магазине один эликсир меньше
        assertEquals(initialShopElixirs - 1, shop.getElixirList().size(), "В магазине должно быть на 1 эликсир меньше");
    }

    @Test
    void testBuyFoodFromShop() {
        // Проверяем, что в магазине есть еда
        int initialShopFood = shop.getFoodList().size();
        assertTrue(initialShopFood > 0, "В магазине должна быть еда для теста");

        // Покупаем еду
        Item food = shop.getFoodList().getFirst();
        int price = shop.priceOf(food);

        backpack.takeItem(food);
        player.setGold(player.getGold() - price);
        shop.removeFood();

        // Проверяем, что еда добавлена в рюкзак
        assertEquals(1, backpack.getFoodList().size(), "В рюкзаке должна быть 1 еда после покупки");
    }

    @Test
    void testBuyScrollFromShop() {
        // Проверяем, что в магазине есть свитки
        int initialShopScrolls = shop.getScrollList().size();
        assertTrue(initialShopScrolls > 0, "В магазине должны быть свитки для теста");

        // Покупаем свиток
        Item scroll = shop.getScrollList().getFirst();
        int price = shop.priceOf(scroll);

        backpack.takeItem(scroll);
        player.setGold(player.getGold() - price);
        shop.removeScroll();

        // Проверяем, что свиток добавлен в рюкзак
        assertEquals(1, backpack.getScrollList().size(), "В рюкзаке должен быть 1 свиток после покупки");
    }

    @Test
    void testBuyWeaponFromShop() {
        // Проверяем, что в магазине есть оружие
        int initialShopWeapons = shop.getWeaponList().size();
        assertTrue(initialShopWeapons > 0, "В магазине должно быть оружие для теста");

        // Покупаем оружие
        Item weapon = shop.getWeaponList().getFirst();
        int price = shop.priceOf(weapon);

        backpack.takeItem(weapon);
        player.setGold(player.getGold() - price);
        shop.removeWeapon();

        // Проверяем, что оружие добавлено в рюкзак
        assertEquals(1, backpack.getWeaponList().size(), "В рюкзаке должно быть 1 оружие после покупки");
    }

    @Test
    void testBackpackCapacityLimit() {
        // Добавляем предметы до предела (9 штук)
        for (int i = 0; i < 9; i++) {
            Item food = new domain.backpack.items.Food(null);
            backpack.takeItem(food);
        }

        // Проверяем, что рюкзак заполнен
        assertEquals(9, backpack.getFoodList().size(), "В рюкзаке должно быть 9 еды");

        // Попытка добавить еще один предмет
        Item extraFood = new domain.backpack.items.Food(null);
        backpack.takeItem(extraFood);

        // Проверяем, что предмет не добавлен (лимит 9)
        assertEquals(9, backpack.getFoodList().size(), "В рюкзаке должно остаться 9 еды (лимит вместимости)");
    }
}
