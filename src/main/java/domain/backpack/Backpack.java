package domain.backpack;

import domain.backpack.ItemsType;
import domain.backpack.items.Elixir;
import domain.backpack.items.Food;
import domain.backpack.items.Scroll;
import domain.backpack.items.Weapon;

import java.util.ArrayList;

public class Backpack {
    private final int maxCapacity = 9;
    ArrayList<Item> elixirList = new ArrayList<>(maxCapacity);
    ArrayList<Item> foodList = new ArrayList<>(maxCapacity);
    ArrayList<Item> scrollList = new ArrayList<>(maxCapacity);
    ArrayList<Item> weaponList = new ArrayList<>(maxCapacity);

    public void takeItem(Item item) {
        if(item instanceof Elixir) {
            if(elixirList.size() < 9) elixirList.add(item);
        } else if(item instanceof Food) {
            if(foodList.size() < 9) foodList.add(item);
        } else if(item instanceof Scroll) {
            if(scrollList.size() < 9) scrollList.add(item);
        } else if(item instanceof Weapon) {
            if(weaponList.size() < 9) weaponList.add(item);
        }
    }

//    public void takeItem(Item item) {
//        switch (item) {
//            case ItemsType.ELIXIR -> addIfPossible(elixirList, item);
//            case ItemsType.F -> addIfPossible(foodList, item);
//            case "Scroll" -> addIfPossible(scrollList, item);
//            case "Weapon" -> addIfPossible(weaponList, item);
//            default -> { }
//        }
//    }
//
//    private void addIfPossible(ArrayList <Item> list, Item item) {
//        if (list.size() < 9) list.add(item);
//    }

    public void seeList() {
        for(var e: elixirList) {
            System.out.println(e);
        }
    }
}