package datalayer.converter;

import datalayer.dto.PlayerDTO;
import domain.characters.Player;

public class PlayerConverter {

    public static PlayerDTO toDTO(Player player) {
        if(player == null) return null;

        PlayerDTO dto = new PlayerDTO();
        dto.setName(player.getName());
        dto.setMaxHealth(player.getMaxHealth());
        dto.setHealth(player.getHealth());
        dto.setBuffAgility(player.getBuffAgility());
        dto.setBuffStrength(player.getBuffStrength());
        dto.setGold(player.getGold());
        dto.setIsStunned(player.getIsStunned());
        dto.setPositionDTO(PositionConverter.toDTO(player.getPosition()));

        return dto;
    }

    public static Player fromDTO(PlayerDTO dto) {
        if(dto == null) return null;

        Player player = new Player(PositionConverter.fromDTO(dto.getPositionDTO()));
        player.setName(dto.getName());
        player.setMaxHealth(dto.getMaxHealth());
        player.setHealth(dto.getHealth());
        player.setBuffAgility(dto.getBuffAgility());
        player.setBuffStrength(dto.getBuffStrength());
        player.setGold(dto.getGold());
        player.setIsStunned(dto.getIsStunned());

        return player;
    }
}
