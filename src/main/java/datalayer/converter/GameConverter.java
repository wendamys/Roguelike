package datalayer.converter;

import datalayer.dto.GameDTO;
import domain.gameSession.Game;
import domain.gameSession.GameFacade;

public class GameConverter {

    public static GameDTO toDTO(GameFacade gameFacade) {
        if(gameFacade == null) return null;

        GameDTO dto = new GameDTO();
        dto.setPlayerDTO(PlayerConverter.toDTO(gameFacade.getPlayer()));
        dto.setBackpackDTO(BackpackConverter.toDTO(gameFacade.getBackpack()));
        dto.setLevelDTO(LevelConverter.toDTO());
        dto.setDungeDTO(DungeConverter.toDTO(gameFacade.getGenerator()));

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
