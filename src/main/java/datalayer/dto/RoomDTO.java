package datalayer.dto;

import domain.characters.Enemies;

import java.util.ArrayList;

public class RoomDTO {
    private int width;
    private int height;
    private ArrayList<EnemiesDTO> enemiesListDTO;
    private ArrayList<ItemDTO> itemListDTO;
    private PositionDTO positionDTO;
    private String doorColor;                        // null, если комната не заперта
    private boolean doorClosed;                      // false, если игрок уже открыл дверь
    private ArrayList<PositionDTO> doorEntrances;    // все входы в запертую комнату

    public RoomDTO() {
        enemiesListDTO = new ArrayList<>();
        itemListDTO = new ArrayList<>();
        doorEntrances = new ArrayList<>();
    }

    public String getDoorColor() {return doorColor;}
    public void setDoorColor(String doorColor) {this.doorColor = doorColor;}

    public boolean isDoorClosed() {return doorClosed;}
    public void setDoorClosed(boolean doorClosed) {this.doorClosed = doorClosed;}

    public ArrayList<PositionDTO> getDoorEntrances() {return doorEntrances;}
    public void setDoorEntrances(ArrayList<PositionDTO> doorEntrances) {this.doorEntrances = doorEntrances;}

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