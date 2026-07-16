package datalayer.dto;

import java.util.ArrayList;
import java.util.List;

public class BackpackDTO {
    private List<ItemDTO> elixirList;
    private List<ItemDTO> foodList;
    private List<ItemDTO> scrollList;
    private List<ItemDTO> weaponList;

    public BackpackDTO() {
        this.elixirList = new ArrayList<>();
        this.foodList = new ArrayList<>();
        this.scrollList = new ArrayList<>();
        this.weaponList = new ArrayList<>();
    }

    public List<ItemDTO> getElixirList() { return elixirList; }
    public void setElixirList(List<ItemDTO> elixirList) { this.elixirList = elixirList; }

    public List<ItemDTO> getFoodList() { return foodList; }
    public void setFoodList(List<ItemDTO> foodList) { this.foodList = foodList; }

    public List<ItemDTO> getScrollList() { return scrollList; }
    public void setScrollList(List<ItemDTO> scrollList) { this.scrollList = scrollList; }

    public List<ItemDTO> getWeaponList() { return weaponList; }
    public void setWeaponList(List<ItemDTO> weaponList) { this.weaponList = weaponList; }
}
