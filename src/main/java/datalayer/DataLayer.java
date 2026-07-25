package datalayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import datalayer.converter.GameConverter;
import datalayer.converter.LeaderboardEntryConverter;
import datalayer.dto.GameDTO;
import datalayer.dto.LeaderboardEntryDTO;
import domain.gameSession.Game;
import domain.leaderboard.LeaderboardEntry;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class DataLayer {
    private static final String FILE_PATH = "data.json";
    private static final String LEADERBOARD_FILE_PATH = "leaderboard.json";
    private static final int LEADERBOARD_MAX_SIZE = 10;
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

    public static GameDTO loadDTO() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            logger.info("Game data loaded successfully from " + FILE_PATH);
            return gson.fromJson(reader, GameDTO.class);
        } catch (IOException | RuntimeException e) {
            System.err.println("Error loading game data: " + e.getMessage());
            return null;
        }
    }

    public static Game load() {
        GameDTO dto = loadDTO();
        if (dto == null) return null;
        return GameConverter.fromDTO(dto);
    }

    /**
     * метод загружает таблицу лидеров из отдельного файла
     * @return список записей, пустой список если файла нет или он повреждён
     */
    public static List<LeaderboardEntry> loadLeaderboard() {
        try (FileReader reader = new FileReader(LEADERBOARD_FILE_PATH)) {
            Type listType = new TypeToken<List<LeaderboardEntryDTO>>() {}.getType();
            List<LeaderboardEntryDTO> dtos = gson.fromJson(reader, listType);
            if (dtos == null) return new ArrayList<>();

            List<LeaderboardEntry> entries = new ArrayList<>();
            dtos.forEach(dto -> entries.add(LeaderboardEntryConverter.fromDTO(dto)));
            return entries;
        } catch (IOException | RuntimeException e) {
            return new ArrayList<>();
        }
    }

    /**
     * метод полностью перезаписывает файл таблицы лидеров переданным списком
     */
    public static void saveLeaderboard(List<LeaderboardEntry> entries) {
        List<LeaderboardEntryDTO> dtos = new ArrayList<>();
        entries.forEach(entry -> dtos.add(LeaderboardEntryConverter.toDTO(entry)));

        try (FileWriter writer = new FileWriter(LEADERBOARD_FILE_PATH, false)) {
            gson.toJson(dtos, writer);
            logger.info("Leaderboard saved to " + LEADERBOARD_FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    /**
     * метод добавляет запись в таблицу лидеров, сортирует по убыванию счёта
     * и обрезает список до LEADERBOARD_MAX_SIZE записей
     */
    public static void addLeaderboardEntry(LeaderboardEntry entry) {
        List<LeaderboardEntry> entries = loadLeaderboard();
        entries.add(entry);
        entries.sort(Comparator.comparingInt(LeaderboardEntry::getScore).reversed());

        List<LeaderboardEntry> trimmed = entries.size() > LEADERBOARD_MAX_SIZE
                ? entries.subList(0, LEADERBOARD_MAX_SIZE)
                : entries;

        saveLeaderboard(trimmed);
    }
}
