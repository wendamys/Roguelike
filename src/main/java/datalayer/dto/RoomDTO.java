package datalayer.dto;

import domain.characters.Enemies;

import java.util.ArrayList;

public class RoomDTO {
    private int width;
    private int height;
    private ArrayList<EnemiesDTO> enemiesListDTO;
    private ArrayList<ItemDTO> itemListDTO;
    private PositionDTO positionDTO;

    public RoomDTO() {
        enemiesListDTO = new ArrayList<>();
        itemListDTO = new ArrayList<>();
    }

    public int getWidth() {return width;}
    public void setWidth(int width) {this.width = width;}

    public int getHeight() {return height;}
    public void setHeight(int height) {this.height = height;}

    public ArrayList<EnemiesDTO> getEnemiesListDTO() {return enemiesListDTO;}
    public void setEnemiesListDTO(ArrayList<EnemiesDTO> enemiesListDTO) {this.enemiesListDTO = enemiesListDTO;}

    public ArrayList<ItemDTO> getItemListDTO() {return itemListDTO;}
    public void setItemListDTO(ArrayList<ItemDTO> itemListDTO) {this.itemListDTO = itemListDTO;}

    public PositionDTO getPositionDTO() {return positionDTO;}
    public void setPositionDTO(PositionDTO positionDTO) {this.positionDTO = positionDTO;}
}