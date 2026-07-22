package datalayer.converter;

import datalayer.dto.PositionDTO;
import datalayer.dto.RoomDTO;
import domain.map.ColorKey;
import domain.map.Door;
import domain.map.Room;
import domain.navigator.Position;

import java.util.ArrayList;
import java.util.List;

public class RoomConverter {
    
    public static RoomDTO toDTO(Room room) {
        if (room == null) return null;
        
        RoomDTO dto = new RoomDTO();
        dto.setWidth(room.getWidth());
        dto.setHeight(room.getHeight());
        dto.setPositionDTO(PositionConverter.toDTO(room.getPosition()));

        room.getEnemyList().forEach(enemy -> dto.getEnemiesListDTO().add(EnemiesConverter.toDTO(enemy)));
        room.getItemList().forEach(item -> dto.getItemListDTO().add(ItemConverter.toDTO(item)));

        Door door = room.getDoor();
        if (door != null) {
            dto.setDoorColor(door.getColorKey().name());
            dto.setDoorClosed(door.getIsClose());
            ArrayList<PositionDTO> entrances = new ArrayList<>();
            door.getEntrances().forEach(pos -> entrances.add(PositionConverter.toDTO(pos)));
            dto.setDoorEntrances(entrances);
        }

        return dto;
    }
    
    public static Room fromDTO(RoomDTO dto) {
        if (dto == null) return null;
        
        Room room = new Room(dto.getPositionDTO().getX(), dto.getPositionDTO().getY());
        room.setWidth(dto.getWidth());
        room.setHeight(dto.getHeight());
        room.setEnemyList(new ArrayList<>());
        room.setItemList(new ArrayList<>());
        
        dto.getEnemiesListDTO().forEach(enemiesDTO -> room.getEnemyList().add(EnemiesConverter.fromDTO(enemiesDTO)));
        dto.getItemListDTO().forEach(itemDTO -> room.getItemList().add(ItemConverter.fromDTO(itemDTO)));

        if (dto.getDoorColor() != null) {
            List<Position> entrances = new ArrayList<>();
            if (dto.getDoorEntrances() != null) {
                dto.getDoorEntrances().forEach(posDTO -> entrances.add(PositionConverter.fromDTO(posDTO)));
            }
            Door door = new Door(room, ColorKey.valueOf(dto.getDoorColor()), entrances);
            door.setClose(dto.isDoorClosed());
            room.setDoor(door);
        }

        return room;
    }
}
