package datalayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datalayer.converter.DungeonGeneratorConverter;
import datalayer.converter.GameConverter;
import datalayer.converter.RoomConverter;
import datalayer.dto.DungeonGeneratorDTO;
import datalayer.dto.GameDTO;
import datalayer.dto.RoomDTO;
import domain.gameSession.Game;
import domain.map.DungeonGenerator;
import domain.map.Room;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class DataLayer {
    private static final String FILE_PATH = "data.json";
    private static final Logger logger = Logger.getLogger(DataLayer.class.getName());
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    
    public DataLayer() {}

    public static void save(Game game) {
        GameDTO gameDTO = GameConverter.toDTO(game);
        try(FileWriter writer = new FileWriter(FILE_PATH, false)) {
            gson.toJson(gameDTO, writer);
            logger.info("Game data saved to " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public static void saveRoom(Room room) {
        RoomDTO roomDTO = RoomConverter.toDTO(room);
        try(FileWriter writer = new FileWriter(FILE_PATH, false)) {
            gson.toJson(roomDTO, writer);
            logger.info("Game data saved to " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public static void saveDungeon(DungeonGenerator dungeon) {
        DungeonGeneratorDTO dto = DungeonGeneratorConverter.toDTO(dungeon);
        try(FileWriter writer = new FileWriter(FILE_PATH, false)) {
            gson.toJson(dto, writer);
            logger.info("Game data saved to " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public static GameDTO loadDTO() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            logger.info("Game data loaded successfully from " + FILE_PATH);
            return gson.fromJson(reader, GameDTO.class);
        } catch (IOException e) {
            System.err.println("Error loading game data: " + e.getMessage());
            return null;
        }
    }

    public static Game load() {
        GameDTO dto = loadDTO();
        if (dto == null) return null;
        return GameConverter.fromDTO(dto);
    }
}
