package datalayer.converter;

import datalayer.dto.LevelDTO;
import domain.map.Level;

public class LevelConverter {
    public static LevelDTO toDTO(Level level) {
        if (level == null) return null;
        LevelDTO dto = new LevelDTO();
        dto.setLevelUp(Level.getLevelUp());
        return dto;
    }

    public static Level fromDTO(LevelDTO dto) {
        if (dto == null) return null;
        Level.setLevelUp(dto.getLevelUp());
        return Level.class != null ? new Level() : null;
    }
}
