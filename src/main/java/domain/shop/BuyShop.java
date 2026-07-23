package domain.shop;

import domain.backpack.Backpack;
import domain.characters.Player;

public class BuyShop {
    private Player player;
    private Shop shop;
    private Backpack backpack;

    public BuyShop(Player player, Shop shop, Backpack backpack) {
        this.player = player;
        this.shop = shop;
        this.backpack = backpack;
    }

    public Player getPlayer() {
        return player;
    }

    public Shop getShop() {
        return shop;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    /**
     * метод покупки элексира и переноса его в рюкзак
     */
    public void buyElixirToBackpack() {
        if (player.getGold() >= shop.priceOf(shop.getElixirList().getFirst()) && !shop.getElixirList().isEmpty()) {
            backpack.takeItem(shop.getElixirList().getFirst());
            player.setGold(player.getGold() - shop.priceOf(shop.getElixirList().getFirst()));
            shop.getElixirList().removeFirst();
        }
    }

    /**
     * метод покупки свитка и переноса его в рюкзак
     */
    public void buyScrollToBackpack() {
        if (player.getGold() >= shop.priceOf(shop.getScrollList().getFirst()) && !shop.getScrollList().isEmpty()) {
            backpack.takeItem(shop.getScrollList().getFirst());
            player.setGold(player.getGold() - shop.priceOf(shop.getScrollList().getFirst()));
            shop.getScrollList().removeFirst();
        }
    }

    /**
     * метод покупки еды и переноса его в рюкзак
     */
    public void buyFoodToBackpack() {
        if (player.getGold() >= shop.priceOf(shop.getFoodList().getFirst()) && !shop.getFoodList().isEmpty()) {
            backpack.takeItem(shop.getFoodList().getFirst());
            player.setGold(player.getGold() - shop.priceOf(shop.getFoodList().getFirst()));
            shop.getFoodList().removeFirst();
        }
    }

    /**
     * метод покупки оружия и переноса его в рюкзак
     */
    public void buyWeaponToBackpack() {
        if (player.getGold() >= shop.priceOf(shop.getWeaponList().getFirst()) && !shop.getWeaponList().isEmpty()) {
            backpack.takeItem(shop.getWeaponList().getFirst());
            player.setGold(player.getGold() - shop.priceOf(shop.getWeaponList().getFirst()));
            shop.getWeaponList().removeFirst();
        }
    }

}
