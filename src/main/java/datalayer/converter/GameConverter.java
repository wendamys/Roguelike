package datalayer.converter;

import datalayer.dto.GameDTO;
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

        return dto;
    }

    public static Game fromDTO(GameDTO dto) {
        if(dto == null) return null;

        Game game = new Game();
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

        return game;
    }
}
