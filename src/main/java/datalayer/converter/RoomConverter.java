package datalayer.converter;

import datalayer.dto.RoomDTO;
import domain.map.Room;

import java.util.ArrayList;

public class RoomConverter {
    
    public static RoomDTO toDTO(Room room) {
        if (room == null) return null;
        
        RoomDTO dto = new RoomDTO();
        dto.setWidth(room.getWidth());
        dto.setHeight(room.getHeight());
        dto.setPositionDTO(PositionConverter.toDTO(room.getPosition()));

        room.getEnemyList().forEach(enemy -> dto.getEnemiesListDTO().add(EnemiesConverter.toDTO(enemy)));
        room.getItemList().forEach(item -> dto.getItemListDTO().add(ItemConverter.toDTO(item)));

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
        
        return room;
    }
}
