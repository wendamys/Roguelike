package domain.shop;

import domain.backpack.Backpack;
import domain.characters.Player;
import domain.gameSession.DifficultyType;
import domain.navigator.Position;
import org.junit.jupiter.api.Test;

public class ShopBuyTest {

    @Test
    void addProduct() {
        Player player = new Player(new Position(0, 0));
        Backpack backpack = new Backpack();
        DifficultyType difficulty = DifficultyType.EASY;
        Shop shop = new Shop(difficulty);

        System.out.println(shop.getElixirList().size() + " " + shop.getElixirList());
        System.out.println(shop.getFoodList().size() + " " + shop.getFoodList());
        System.out.println(shop.getScrollList().size() + " " + shop.getScrollList());
        System.out.println(shop.getWeaponList().size() + " " + shop.getWeaponList());
    }
}
