package datalayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.util.ISO8601Utils;
import datalayer.dto.GameDTO;
import datalayer.dto.PositionDTO;
import domain.navigator.Position;

import java.io.File;
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

    public static void save(GameDTO gameDTO) {
        try(FileWriter writer = new FileWriter(FILE_PATH, false)) {
            gson.toJson(gameDTO, writer);
            logger.info("Position data saved " + FILE_PATH);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static GameDTO load() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            logger.info("Position data loaded successfully from " + FILE_PATH);
            return gson.fromJson(reader, GameDTO.class);
        } catch (IOException e) {
            System.err.println("Error loading game data: " + e.getMessage());
            return null;
        }
    }
}
