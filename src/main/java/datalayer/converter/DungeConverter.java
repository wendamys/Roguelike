package datalayer.converter;

import datalayer.dto.*;
import domain.backpack.Item;
import domain.characters.Enemies;
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
        generator.setRooms(rooms);

        ArrayList<Corridor> corridors = new ArrayList<>();
        ArrayList<Room> roomListCopy = new ArrayList<>(rooms);
        dto.getCorridorsDTO().forEach(corridorDTO -> {
            Room room1 = findRoomByPosition(roomListCopy, corridorDTO.getRoom1PositionDTO());
            Room room2 = findRoomByPosition(roomListCopy, corridorDTO.getRoom2PositionDTO());
            if (room1 != null && room2 != null) {
                corridors.add(CorridorConverter.fromDTO(corridorDTO, room1, room2));
            }
        });
        generator.setCorridors(corridors);
        
        // Восстанавливаем всех врагов в комнаты
        ArrayList<Enemies> allEnemies = new ArrayList<>();
        dto.getAllEnemiesListDTO().forEach(enemyDTO -> allEnemies.add(EnemiesConverter.fromDTO(enemyDTO)));
        // Распределяем врагов по комнатам (по позиции)
        for (Enemies enemy : allEnemies) {
            for (Room room : rooms) {
                if (enemy.getPosition().getX() >= room.getPosition().getX() && 
                    enemy.getPosition().getX() < room.getPosition().getX() + room.getWidth() &&
                    enemy.getPosition().getY() >= room.getPosition().getY() && 
                    enemy.getPosition().getY() < room.getPosition().getY() + room.getHeight()) {
                    room.getEnemyList().add(enemy);
                    break;
                }
            }
        }
        
        // Восстанавливаем все предметы в комнаты
        ArrayList<Item> allItems = new ArrayList<>();
        dto.getAllItemsListDTO().forEach(itemDTO -> allItems.add(ItemConverter.fromDTO(itemDTO)));
        // Распределяем предметы по комнатам (по позиции)
        for (domain.backpack.Item item : allItems) {
            for (Room room : rooms) {
                if (item.getPosition().getX() >= room.getPosition().getX() && 
                    item.getPosition().getX() < room.getPosition().getX() + room.getWidth() &&
                    item.getPosition().getY() >= room.getPosition().getY() && 
                    item.getPosition().getY() < room.getPosition().getY() + room.getHeight()) {
                    room.getItemList().add(item);
                    break;
                }
            }
        }
        
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
