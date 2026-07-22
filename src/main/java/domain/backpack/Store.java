package domain.backpack;

import domain.backpack.items.Elixir;
import domain.backpack.items.Food;
import domain.backpack.items.Scroll;
import domain.backpack.items.Weapon;
import domain.navigator.Position;

import java.util.ArrayList;

import static domain.MathUtils.MathUtils.randomNumber;

public class Store {
    private final int maxCapacity = randomNumber(0, 4);
    private final ArrayList<Item> elixirList = new ArrayList<>(maxCapacity);
    private final ArrayList<Item> foodList = new ArrayList<>(maxCapacity);
    private final ArrayList<Item> scrollList = new ArrayList<>(maxCapacity);
    private final ArrayList<Item> weaponList = new ArrayList<>(maxCapacity);
    private Backpack backpack;
    private Position position;

    public String getName() {return "$";}

    public int getMaxCapacity () {return maxCapacity;}

    public ArrayList<Item> getElixirList() {return elixirList;}
    public ArrayList<Item> getFoodList() {return foodList;}
    public ArrayList<Item> getScrollList() {return scrollList;}
    public ArrayList<Item> getWeaponList() {return weaponList;}

    public Backpack getBackpack() {return backpack;}
    public void setBackpack(Backpack backpack) {this.backpack = backpack;}

    public Position getPosition() {return position;}
    public void setPosition(Position position) {this.position = position;}


    public void addElixirBackpack() {
        if (backpack.getElixirList().size() < 9)
            backpack.getElixirList().add(new Elixir(null));
    }

    public void addScrollToBackpack() {
        if (backpack.getScrollList().size() < 9)
            backpack.getScrollList().add(new Scroll(null));
    }

    public void addFoodToBackpack() {
        if (backpack.getFoodList().size() < 9)
            backpack.getFoodList().add(new Food(null));
    }

    public void addWeaponToBackpack() {
        if (backpack.getWeaponList().size() < 9)
            backpack.getWeaponList().add(new Weapon(null));
    }
}
