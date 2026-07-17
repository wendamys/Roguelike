package datalayer.dto;

import java.util.ArrayList;

public class BackpackDTO {
    private ArrayList<ItemDTO> elixirListDTO;
    private ArrayList<ItemDTO> scrollListDTO;
    private ArrayList<ItemDTO> foodListDTO;
    private ArrayList<ItemDTO> weaponListDTO;

    public BackpackDTO() {
        elixirListDTO = new ArrayList<>();
        scrollListDTO = new ArrayList<>();
        foodListDTO = new ArrayList<>();
        weaponListDTO = new ArrayList<>();
    }

    public ArrayList<ItemDTO> getElixirListDTO() {return elixirListDTO;}
    public void setElixirListDTO(ArrayList<ItemDTO> elixirListDTO) {this.elixirListDTO = elixirListDTO;}

    public ArrayList<ItemDTO> getScrollListDTO() {return scrollListDTO;}
    public void setScrollListDTO(ArrayList<ItemDTO> scrollListDTO) {this.scrollListDTO = scrollListDTO;}

    public ArrayList<ItemDTO> getFoodListDTO() {return foodListDTO;}
    public void setFoodListDTO(ArrayList<ItemDTO> foodListDTO) {this.foodListDTO = foodListDTO;}

    public ArrayList<ItemDTO> getWeaponListDTO() {return weaponListDTO;}
    public void setWeaponListDTO(ArrayList<ItemDTO> weaponListDTO) {this.weaponListDTO = weaponListDTO;}
}
