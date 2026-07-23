package domain.shop;

import domain.backpack.Backpack;
import domain.backpack.Item;
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
        if (shop.getElixirList().isEmpty() || backpack.getElixirList().size() == 9) return;
        Item item = shop.getElixirList().getFirst();
        if (player.getGold() >= shop.priceOf(item)) {
            backpack.takeItem(item);
            player.setGold(player.getGold() - shop.priceOf(item));
            shop.removeElixir();
        }
    }

    /**
     * метод покупки свитка и переноса его в рюкзак
     */
    public void buyScrollToBackpack() {
        if (shop.getScrollList().isEmpty() || backpack.getScrollList().size() == 9) return;
        Item item = shop.getScrollList().getFirst();
        if (player.getGold() >= shop.priceOf(item)) {
            backpack.takeItem(item);
            player.setGold(player.getGold() - shop.priceOf(item));
            shop.removeScroll();
        }
    }

    /**
     * метод покупки еды и переноса его в рюкзак
     */
    public void buyFoodToBackpack() {
        if (shop.getFoodList().isEmpty() || backpack.getFoodList().size() == 9) return;
        Item item = shop.getFoodList().getFirst();
        if (player.getGold() >= shop.priceOf(item)) {
            backpack.takeItem(item);
            player.setGold(player.getGold() - shop.priceOf(item));
            shop.removeFood();
        }
    }

    /**
     * метод покупки оружия и переноса его в рюкзак
     */
    public void buyWeaponToBackpack() {
        if (shop.getWeaponList().isEmpty() || backpack.getWeaponList().size() == 9) return;
        Item item = shop.getWeaponList().getFirst();
        if (player.getGold() >= shop.priceOf(item)) {
            backpack.takeItem(item);
            player.setGold(player.getGold() - shop.priceOf(item));
            shop.removeWeapon();
        }
    }

}
