package datalayer.converter;

import datalayer.dto.PositionDTO;
import domain.navigator.Position;

public class PositionConverter {
    public static PositionDTO toDTO(Position position) {
        if (position == null) return null;
        PositionDTO dto = new PositionDTO();
        dto.setX(position.getX());
        dto.setY(position.getY());
        dto.setIsClose(position.getIsClose());
        return dto;
    }

    public static Position fromDTO(PositionDTO dto) {
        if (dto == null) return null;
        Position position = new Position(dto.getX(), dto.getY());
        position.setIsClose(dto.getIsClose());
        return position;
    }
}
