package domain.shop;

import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.gameSession.DifficultyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShopTest {

    @RepeatedTest(20)
    @DisplayName("Количество предметов каждого типа укладывается в диапазон сложности")
    void assortmentRespectsDifficulty() {
        for (DifficultyType difficulty : DifficultyType.values()) {
            Shop shop = new Shop(difficulty);
            for (ItemsType type : ItemsType.values()) {
                long count = shop.getItems().stream()
                        .filter(item -> item.getType() == type)
                        .count();
                assertTrue(count >= difficulty.getShopMin() && count <= difficulty.getShopMax(),
                        "Тип " + type + " на " + difficulty + ": " + count);
            }
        }
    }

    @RepeatedTest(5)
    @DisplayName("Цена предмета положительна")
    void priceIsPositive() {
        Shop shop = new Shop(DifficultyType.EASY);
        for (Item item : shop.getItems()) {
            assertTrue(shop.priceOf(item) > 0);
        }
    }
}
