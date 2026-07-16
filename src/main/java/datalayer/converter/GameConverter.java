package datalayer.converter;

import datalayer.dto.GameDTO;
import domain.gameSession.Game;

public class GameConverter {
    public static GameDTO toDTO(Game game) {
        if (game == null) return null;
        
        GameDTO dto = new GameDTO();
        dto.setLevelDTO(LevelConverter.toDTO(null));
        dto.setPlayerDTO(PlayerConverter.toDTO(game.getPlayer()));
        dto.setBackpackDTO(BackpackConverter.toDTO(game.getBackpack()));
        dto.setPosLevel(PositionConverter.toDTO(game.getPosLevel()));
        dto.setGameEnded(game.isGameEnded());
        
        return dto;
    }

    public static Game fromDTO(GameDTO dto) {
        if (dto == null) return null;
        Game game = new Game();
        
        if (dto.getPlayerDTO() != null) {
            game.getPlayer().setName(dto.getPlayerDTO().getName());
            game.getPlayer().setMaxHealth(dto.getPlayerDTO().getMaxHealth());
            game.getPlayer().setHealth(dto.getPlayerDTO().getHealth());
            game.getPlayer().setBuffAgility(dto.getPlayerDTO().getBuffAgility());
            game.getPlayer().setBuffStrength(dto.getPlayerDTO().getBuffStrength());
            game.getPlayer().setGold(dto.getPlayerDTO().getGold());
            game.getPlayer().setStunned(dto.getPlayerDTO().isStunned());
            game.getPlayer().setPosition(PositionConverter.fromDTO(dto.getPlayerDTO().getPosition()));
        }
        
        if (dto.getBackpackDTO() != null) {
            BackpackConverter.fromDTO(dto.getBackpackDTO(), game.getBackpack());
        }
        
        return game;
    }
}
