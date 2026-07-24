package datalayer.converter;

import datalayer.converter.BackpackConverter;
import datalayer.converter.DungeConverter;
import datalayer.converter.LevelConverter;
import datalayer.converter.PlayerConverter;
import datalayer.converter.PositionConverter;
import datalayer.converter.ShopConverter;
import datalayer.dto.GameDTO;
import datalayer.dto.KeyDTO;
import domain.gameSession.DifficultyType;
import domain.gameSession.Game;
import domain.map.ColorKey;
import domain.map.DungeonGenerator;
import domain.map.Key;
import domain.shop.Shop;
import domain.shop.BuyShop;

import java.util.ArrayList;

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
        dto.setEnemiesKilled(game.getEnemiesKilled());

        ArrayList<KeyDTO> keysDTO = new ArrayList<>();
        game.getGenerator().getKeys().forEach(key -> {
            KeyDTO keyDTO = new KeyDTO();
            keyDTO.setColor(key.getColorKey().name());
            keyDTO.setPositionDTO(PositionConverter.toDTO(key.getPosition()));
            keysDTO.add(keyDTO);
        });
        dto.setKeysDTO(keysDTO);
        dto.setShopPositionDTO(PositionConverter.toDTO(game.getGenerator().getShopPosition()));
        dto.setShopDTO(ShopConverter.toDTO(game.getShop()));

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

        // ключи восстанавливаем до placeRestoredEntitiesOnMap - он рисует их на карте
        generator.getKeys().clear();
        if (dto.getKeysDTO() != null) {
            dto.getKeysDTO().forEach(keyDTO -> generator.getKeys().add(
                    new Key(PositionConverter.fromDTO(keyDTO.getPositionDTO()),
                            ColorKey.valueOf(keyDTO.getColor()))));
        }

        if (dto.getShopPositionDTO() != null) {
            generator.setShopPosition(PositionConverter.fromDTO(dto.getShopPositionDTO()));
        }

        if (dto.getShopDTO() != null) {
            game.setShop(ShopConverter.fromDTO(dto.getShopDTO()));
        }

        // Обновляем buyShop после загрузки магазина
        if (game.getShop() != null) {
            game.setBuyShop(new BuyShop(game.getPlayer(), game.getShop(), game.getBackpack()));
        }

        game.restoreItemsAndEnemies();
        game.placeRestoredEntitiesOnMap();

        if (dto.getExplored() != null) {
            game.getFog().setExplored(dto.getExplored());
        }
        game.setEnemiesKilled(dto.getEnemiesKilled());

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
