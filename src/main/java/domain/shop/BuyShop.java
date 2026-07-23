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
        if (player.getGold() >= shop.priceOf(shop.getElixirList().get(0)) && !shop.getElixirList().isEmpty()) {
            backpack.takeItem(shop.getElixirList().get(0));
            player.setGold(player.getGold() - shop.priceOf(shop.getElixirList().get(0)));
            shop.getElixirList().remove(0);
        }
    }

    /**
     * метод покупки свитка и переноса его в рюкзак
     */
    public void buyScrollToBackpack() {
        if (player.getGold() >= shop.priceOf(shop.getScrollList().get(0)) && !shop.getScrollList().isEmpty()) {
            backpack.takeItem(shop.getScrollList().get(0));
            player.setGold(player.getGold() - shop.priceOf(shop.getScrollList().get(0)));
            shop.getScrollList().remove(0);
        }
    }

    /**
     * метод покупки еды и переноса его в рюкзак
     */
    public void buyFoodToBackpack() {
        if (player.getGold() >= shop.priceOf(shop.getFoodList().get(0)) && !shop.getFoodList().isEmpty()) {
            backpack.takeItem(shop.getFoodList().get(0));
            player.setGold(player.getGold() - shop.priceOf(shop.getFoodList().get(0)));
            shop.getFoodList().remove(0);
        }
    }

    /**
     * метод покупки оружия и переноса его в рюкзак
     */
    public void buyWeaponToBackpack() {
        if (player.getGold() >= shop.priceOf(shop.getWeaponList().get(0)) && !shop.getWeaponList().isEmpty()) {
            backpack.takeItem(shop.getWeaponList().get(0));
            player.setGold(player.getGold() - shop.priceOf(shop.getWeaponList().get(0)));
            shop.getWeaponList().remove(0);
        }
    }

}
