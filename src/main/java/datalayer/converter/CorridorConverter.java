package datalayer.converter;

import datalayer.dto.CorridorDTO;
import domain.map.Corridor;
import domain.map.Room;

import java.util.ArrayList;

public class CorridorConverter {
    
    public static CorridorDTO toDTO(Corridor corridor) {
        if (corridor == null) return null;
        
        CorridorDTO dto = new CorridorDTO();
        
        corridor.getPath().forEach(position -> dto.getPathDTO().add(PositionConverter.toDTO(position)));
        dto.setRoom1PositionDTO(PositionConverter.toDTO(corridor.getRoom1().getPosition()));
        dto.setRoom2PositionDTO(PositionConverter.toDTO(corridor.getRoom2().getPosition()));
        
        return dto;
    }
    
    public static Corridor fromDTO(CorridorDTO dto, Room room1, Room room2) {
        if (dto == null || room1 == null || room2 == null) return null;
        
        Corridor corridor = new Corridor(room1, room2);
        corridor.getPath().clear();
        
        dto.getPathDTO().forEach(positionDTO -> corridor.getPath().add(PositionConverter.fromDTO(positionDTO)));
        
        return corridor;
    }
}
