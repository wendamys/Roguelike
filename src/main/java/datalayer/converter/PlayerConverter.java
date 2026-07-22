package datalayer.converter;

import datalayer.dto.PlayerDTO;
import domain.characters.Player;
import domain.map.ColorKey;

import java.util.ArrayList;

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

        ArrayList<String> keys = new ArrayList<>();
        player.getKeys().forEach(key -> keys.add(key.name()));
        dto.setKeys(keys);

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

        if (dto.getKeys() != null) {
            dto.getKeys().forEach(name -> player.addKey(ColorKey.valueOf(name)));
        }

        return player;
    }
}
