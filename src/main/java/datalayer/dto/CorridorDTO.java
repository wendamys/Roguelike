package datalayer.dto;

import java.util.ArrayList;

public class CorridorDTO {
    private ArrayList<PositionDTO> pathDTO;
    private PositionDTO room1PositionDTO;
    private PositionDTO room2PositionDTO;

    public CorridorDTO() {
        pathDTO = new ArrayList<>();
    }

    public ArrayList<PositionDTO> getPathDTO() {return pathDTO;}
    public void setPathDTO(ArrayList<PositionDTO> pathDTO) {this.pathDTO = pathDTO;}
    
    public PositionDTO getRoom1PositionDTO() {return room1PositionDTO;}
    public void setRoom1PositionDTO(PositionDTO room1PositionDTO) {this.room1PositionDTO = room1PositionDTO;}
    
    public PositionDTO getRoom2PositionDTO() {return room2PositionDTO;}
    public void setRoom2PositionDTO(PositionDTO room2PositionDTO) {this.room2PositionDTO = room2PositionDTO;}
}
