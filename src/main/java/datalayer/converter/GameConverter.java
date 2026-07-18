package datalayer.converter;

import datalayer.dto.GameDTO;
import domain.gameSession.Game;

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
        game.setGenerator(DungeConverter.fromDTO(dto.getDungeDTO()));

        return game;
    }
}
