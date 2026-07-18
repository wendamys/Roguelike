package datalayer.dto;

import java.util.ArrayList;

public class DungeDTO {
    private ArrayList<RoomDTO> roomsDTO;
    private ArrayList<CorridorDTO> corridorsDTO;
    private ArrayList<EnemiesDTO> allEnemiesListDTO;
    private ArrayList<ItemDTO> allItemsListDTO; // <--

    public ArrayList<RoomDTO> getRoomsDTO() {return roomsDTO;}
    public void setRoomsDTO(ArrayList<RoomDTO> roomsDTO) {this.roomsDTO = roomsDTO;}
    
    public ArrayList<CorridorDTO> getCorridorsDTO() {return corridorsDTO;}
    public void setCorridorsDTO(ArrayList<CorridorDTO> corridorsDTO) {this.corridorsDTO = corridorsDTO;}

    public ArrayList<EnemiesDTO> getAllEnemiesListDTO() {return allEnemiesListDTO;}
    public void setAllEnemiesListDTO(ArrayList<EnemiesDTO> allEnemiesListDTO) {this.allEnemiesListDTO = allEnemiesListDTO;}

    public ArrayList<ItemDTO> getAllItemsListDTO() {return allItemsListDTO;}
    public void setAllItemsListDTO(ArrayList<ItemDTO> allItemsListDTO) {this.allItemsListDTO = allItemsListDTO;}
}
