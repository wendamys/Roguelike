package datalayer.converter;

import datalayer.dto.PositionDTO;
import domain.navigator.Position;

public class PositionConverter {

    public static PositionDTO toDTO(Position position) {
        if(position == null) return null;

        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setX(position.getX());
        positionDTO.setY(position.getY());

        return positionDTO;
    }

    public static Position fromDTO(PositionDTO dto) {
        if(dto == null) return null;

        Position position = new Position(dto.getX(), dto.getY());

        return position;
    }
}
