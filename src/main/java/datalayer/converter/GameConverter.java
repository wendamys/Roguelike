package datalayer.converter;

import datalayer.dto.GameDTO;
import domain.gameSession.DifficultyType;
import domain.gameSession.Game;
import domain.map.DungeonGenerator;

public class GameConverter {

    public static GameDTO toDTO(Game game) {
        if(game == null) return null;

        GameDTO dto = new GameDTO();
        dto.setPlayerDTO(PlayerConverter.toDTO(game.getPlayer()));
        dto.setBackpackDTO(BackpackConverter.toDTO(game.getBackpack()));
        dto.setLevelDTO(LevelConverter.toDTO());
        dto.setDungeDTO(DungeConverter.toDTO(game.getGenerator()));
        dto.setDifficulty(game.getDifficulty().name());
        dto.setExplored(game.getFog().getExplored());

        return dto;
    }

    public static Game fromDTO(GameDTO dto) {
        if(dto == null) return null;

        Game game = new Game(parseDifficulty(dto.getDifficulty()));
        game.setPlayer(PlayerConverter.fromDTO(dto.getPlayerDTO()));
        game.setBackpack(BackpackConverter.fromDTO(dto.getBackpackDTO()));
        LevelConverter.fromDTO(dto.getLevelDTO());
        // Game.level всегда на 1 больше отображаемого Level.getLevelUp() (см. generateNewLevel: level++ пост-инкремент)
        game.setLevel(dto.getLevelDTO().getLevelUp() + 1);

        DungeonGenerator generator = DungeConverter.fromDTO(dto.getDungeDTO());
        game.setGenerator(generator);
        game.setRooms(generator.getRooms());

        game.restoreItemsAndEnemies();
        game.placeRestoredEntitiesOnMap();

        if (dto.getExplored() != null) {
            game.getFog().setExplored(dto.getExplored());
        }

        return game;
    }

    /**
     * метод разбирает сохранённую сложность, старые сейвы без неё считаются EASY
     * @param name имя значения перечисления
     * @return уровень сложности
     */
    private static DifficultyType parseDifficulty(String name) {
        if (name == null) return DifficultyType.EASY;
        try {
            return DifficultyType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return DifficultyType.EASY;
        }
    }
}
