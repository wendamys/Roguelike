package datalayer.converter;

import datalayer.dto.LevelDTO;
import domain.map.Level;

public class LevelConverter {
    
    public static LevelDTO toDTO() {
        LevelDTO dto = new LevelDTO();
        dto.setLevelUp(Level.getLevelUp());
        return dto;
    }
    
    public static void fromDTO(LevelDTO dto) {
        if (dto != null) {
            Level.setLevelUp(dto.getLevelUp());
        }
    }
}
