package datalayer.dto;

import java.util.ArrayList;

public class DungeonGeneratorDTO {
    private ArrayList<RoomDTO> roomsDTO;
    private ArrayList<CorridorDTO> corridorsDTO;

    public ArrayList<RoomDTO> getRoomsDTO() {return roomsDTO;}
    public void setRoomsDTO(ArrayList<RoomDTO> roomsDTO) {this.roomsDTO = roomsDTO;}
    
    public ArrayList<CorridorDTO> getCorridorsDTO() {return corridorsDTO;}
    public void setCorridorsDTO(ArrayList<CorridorDTO> corridorsDTO) {this.corridorsDTO = corridorsDTO;}
}
