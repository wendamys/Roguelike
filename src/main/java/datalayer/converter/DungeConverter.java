package datalayer.converter;

import datalayer.dto.*;
import domain.map.Corridor;
import domain.map.DungeonGenerator;
import domain.map.Room;

import java.util.ArrayList;

public class DungeConverter {
    
    public static DungeDTO toDTO(DungeonGenerator generator) {
        if (generator == null) return null;
        
        DungeDTO dto = new DungeDTO();

        ArrayList<RoomDTO> roomsDTO = new ArrayList<>();
        generator.getRooms().forEach(room -> roomsDTO.add(RoomConverter.toDTO(room)));
        dto.setRoomsDTO(roomsDTO);

        ArrayList<CorridorDTO> corridorsDTO = new ArrayList<>();
        generator.getCorridors().forEach(corridor -> corridorsDTO.add(CorridorConverter.toDTO(corridor)));
        dto.setCorridorsDTO(corridorsDTO);
        
        // Собираем всех врагов из всех комнат
        ArrayList<EnemiesDTO> allEnemiesListDTO = new ArrayList<>();
        generator.getRooms().forEach(room -> room.getEnemyList().forEach(enemy -> allEnemiesListDTO.add(EnemiesConverter.toDTO(enemy))));
        dto.setAllEnemiesListDTO(allEnemiesListDTO);
        
        // Собираем все предметы из всех комнат
        ArrayList<ItemDTO> allItemsListDTO = new ArrayList<>();
        generator.getRooms().forEach(room -> room.getItemList().forEach(item -> allItemsListDTO.add(ItemConverter.toDTO(item))));
        dto.setAllItemsListDTO(allItemsListDTO);
        
        return dto;
    }
    
    public static DungeonGenerator fromDTO(DungeDTO dto) {
        if (dto == null) return null;
        
        DungeonGenerator generator = new DungeonGenerator();

        ArrayList<Room> rooms = new ArrayList<>();
        dto.getRoomsDTO().forEach(roomDTO -> rooms.add(RoomConverter.fromDTO(roomDTO)));

        ArrayList<Corridor> corridors = new ArrayList<>();
        ArrayList<Room> roomListCopy = new ArrayList<>(rooms);
        dto.getCorridorsDTO().forEach(corridorDTO -> {
            Room room1 = findRoomByPosition(roomListCopy, corridorDTO.getRoom1PositionDTO());
            Room room2 = findRoomByPosition(roomListCopy, corridorDTO.getRoom2PositionDTO());
            if (room1 != null && room2 != null) {
                corridors.add(CorridorConverter.fromDTO(corridorDTO, room1, room2));
            }
        });

        generator.rebuildMap(rooms, corridors);

        return generator;
    }
    
    private static Room findRoomByPosition(ArrayList<Room> rooms, PositionDTO positionDTO) {
        if (positionDTO == null) return null;
        for (Room room : rooms) {
            if (room.getPosition().getX() == positionDTO.getX() && 
                room.getPosition().getY() == positionDTO.getY()) {
                return room;
            }
        }
        return null;
    }
}
