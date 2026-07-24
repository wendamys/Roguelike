package datalayer.dto;

import java.util.ArrayList;

public class ShopDTO {
    private ArrayList<ItemDTO> elixirList = new ArrayList<>();
    private ArrayList<ItemDTO> foodList = new ArrayList<>();
    private ArrayList<ItemDTO> scrollList = new ArrayList<>();
    private ArrayList<ItemDTO> weaponList = new ArrayList<>();
    private String difficulty;

    public ShopDTO() {}

    public ArrayList<ItemDTO> getElixirList() { return elixirList; }
    public void setElixirList(ArrayList<ItemDTO> elixirList) { this.elixirList = elixirList; }

    public ArrayList<ItemDTO> getFoodList() { return foodList; }
    public void setFoodList(ArrayList<ItemDTO> foodList) { this.foodList = foodList; }

    public ArrayList<ItemDTO> getScrollList() { return scrollList; }
    public void setScrollList(ArrayList<ItemDTO> scrollList) { this.scrollList = scrollList; }

    public ArrayList<ItemDTO> getWeaponList() { return weaponList; }
    public void setWeaponList(ArrayList<ItemDTO> weaponList) { this.weaponList = weaponList; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}
