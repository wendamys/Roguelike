package datalayer.dto;

import java.util.ArrayList;

public class GameDTO {
    private PlayerDTO playerDTO;
    private BackpackDTO backpackDTO;
    private ArrayList<RoomDTO> allRoomListDTO;

    public PlayerDTO getPlayerDTO() { return playerDTO; }
    public void setPlayerDTO(PlayerDTO playerDTO) { this.playerDTO = playerDTO; }

    public BackpackDTO getBackpackDTO() { return backpackDTO; }
    public void setBackpackDTO(BackpackDTO backpackDTO) {this.backpackDTO = backpackDTO; }

    public ArrayList<RoomDTO> getAllRoomListDTO() {return allRoomListDTO;}
    public void setAllRoomListDTO(ArrayList<RoomDTO> allRoomListDTO) {this.allRoomListDTO = allRoomListDTO;}
}