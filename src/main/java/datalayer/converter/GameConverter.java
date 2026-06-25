package datalayer.converter;

import datalayer.dto.GameDTO;
import datalayer.dto.PositionDTO;
import domain.gameSession.GameSession;
import domain.navigator.Position;

public class GameConverter {
    public static GameDTO toDTO(GameSession gameSession) {
        GameDTO dto = new GameDTO();
        return dto;
    }

    public static GameSession fromDTO(GameDTO dto) {
        GameSession gameSession = new GameSession();
        return gameSession;
    }
}
