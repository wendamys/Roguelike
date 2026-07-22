package datalayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datalayer.converter.GameConverter;
import datalayer.dto.GameDTO;
import domain.gameSession.DifficultyType;
import domain.gameSession.Game;
import domain.map.ColorKey;
import domain.map.Door;
import domain.map.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Проверяет, что новые поля переживают сериализацию в JSON и обратно.
 * Работает через Gson напрямую, не трогая пользовательский data.json.
 */
public class SaveLoadRoundTripTest {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Game roundTrip(Game game) {
        GameDTO dto = GameConverter.toDTO(game);
        GameDTO restored = gson.fromJson(gson.toJson(dto), GameDTO.class);
        return GameConverter.fromDTO(restored);
    }

    @Test
    @DisplayName("Сложность и счётчик убитых переживают сохранение")
    void difficultyAndKillsSurvive() {
        Game game = new Game(DifficultyType.VERY_HARD);
        game.setEnemiesKilled(7);
        game.getPlayer().setGold(42);

        Game loaded = roundTrip(game);

        assertEquals(DifficultyType.VERY_HARD, loaded.getDifficulty());
        assertEquals(7, loaded.getEnemiesKilled());
        assertEquals(42, loaded.getPlayer().getGold());
    }

    @Test
    @DisplayName("Разведанная карта переживает сохранение")
    void exploredSurvives() {
        Game game = new Game(DifficultyType.EASY);

        Game loaded = roundTrip(game);

        // на EASY туман открыт целиком, значит и после загрузки карта разведана
        assertTrue(loaded.getFog().isExplored(0, 0));
        assertTrue(loaded.getFog().isExplored(69, 59));
    }

    @Test
    @DisplayName("Ключи игрока переживают сохранение")
    void playerKeysSurvive() {
        Game game = new Game(DifficultyType.EASY);
        game.getPlayer().addKey(ColorKey.RED);
        game.getPlayer().addKey(ColorKey.BLUE);

        Game loaded = roundTrip(game);

        assertTrue(loaded.getPlayer().hasKey(ColorKey.RED));
        assertTrue(loaded.getPlayer().hasKey(ColorKey.BLUE));
        assertFalse(loaded.getPlayer().hasKey(ColorKey.GREEN));
    }

    @Test
    @DisplayName("Двери и ключи на карте переживают сохранение")
    void doorsAndMapKeysSurvive() {
        Game game = new Game(DifficultyType.EASY);
        int lockedBefore = countLockedRooms(game);
        int keysBefore = game.getGenerator().getKeys().size();

        Game loaded = roundTrip(game);

        assertEquals(lockedBefore, countLockedRooms(loaded), "Число запертых комнат должно совпасть");
        assertEquals(keysBefore, loaded.getGenerator().getKeys().size(), "Число ключей должно совпасть");

        for (Room room : loaded.getRooms()) {
            Door door = room.getDoor();
            if (door == null || !door.getIsClose()) {
                continue;
            }
            assertNotNull(door.getColorKey());
            assertFalse(door.getEntrances().isEmpty(), "У запертой двери должны быть входы");
            door.getEntrances().forEach(entrance ->
                    assertFalse(loaded.getGenerator().isPositionWalkable(entrance),
                            "Закрытая дверь должна остаться непроходимой после загрузки"));
        }
    }

    private int countLockedRooms(Game game) {
        return (int) game.getRooms().stream().filter(room -> room.getDoor() != null).count();
    }
}
