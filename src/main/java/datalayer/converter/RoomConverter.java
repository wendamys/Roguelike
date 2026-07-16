package datalayer.converter;

import datalayer.dto.RoomDTO;
import datalayer.dto.RoomTypeDTO;
import domain.map.Room;
import domain.map.RoomType;
import domain.navigator.Position;

public class RoomConverter {
    public static RoomDTO toDTO(Room room) {
        if (room == null) return null;
        RoomDTO dto = new RoomDTO();
        dto.setWidth(room.getWidth());
        dto.setHeight(room.getHeight());
        dto.setArea(room.getArea());
        dto.setCapacityEnemy(room.getCapacityEnemy());
        dto.setCapacityItem(room.getCapacityItem());
        dto.setRoomType(room.getRoomType() != null ? RoomTypeDTO.valueOf(room.getRoomType().name()) : null);
        dto.setPosition(PositionConverter.toDTO(room.getPosition()));
        return dto;
    }

    public static Room fromDTO(RoomDTO dto) {
        if (dto == null || dto.getPosition() == null) return null;
        
        // Create room with position from DTO
        Room room = new Room(dto.getPosition().getX(), dto.getPosition().getY());
        
        // Update room properties from DTO
        if (dto.getRoomType() != null) {
            try {
                room.setRoomType(dto.getArea()); // Use area to set room type
            } catch (Exception e) {
                // Ignore if room type cannot be set
            }
        }
        
        room.setCapacityEnemy(dto.getCapacityEnemy());
        room.setCapacityItem(dto.getCapacityItem());
        
        return room;
    }
}
