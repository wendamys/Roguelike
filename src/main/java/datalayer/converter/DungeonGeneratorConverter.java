package datalayer.converter;

import datalayer.dto.CorridorDTO;
import datalayer.dto.DungeonGeneratorDTO;
import datalayer.dto.PositionDTO;
import datalayer.dto.RoomDTO;
import domain.map.Corridor;
import domain.map.DungeonGenerator;
import domain.map.Room;

import java.util.ArrayList;

public class DungeonGeneratorConverter {
    
    public static DungeonGeneratorDTO toDTO(DungeonGenerator generator) {
        if (generator == null) return null;
        
        DungeonGeneratorDTO dto = new DungeonGeneratorDTO();

        ArrayList<RoomDTO> roomsDTO = new ArrayList<>();
        generator.getRooms().forEach(room -> roomsDTO.add(RoomConverter.toDTO(room)));
        dto.setRoomsDTO(roomsDTO);

        ArrayList<CorridorDTO> corridorsDTO = new ArrayList<>();
        generator.getCorridors().forEach(corridor -> corridorsDTO.add(CorridorConverter.toDTO(corridor)));
        dto.setCorridorsDTO(corridorsDTO);
        
        return dto;
    }
    
    public static DungeonGenerator fromDTO(DungeonGeneratorDTO dto) {
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
