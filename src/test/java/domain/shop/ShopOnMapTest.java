package domain.shop;

import domain.gameSession.DifficultyType;
import domain.gameSession.Game;
import domain.map.DungeonGenerator;
import domain.map.Room;
import domain.map.TileType;
import domain.navigator.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Магазин - отдельная точка на уровне, а не всегда доступная панель.
 */
public class ShopOnMapTest {

    @RepeatedTest(20)
    @DisplayName("Магазин стоит на карте своим тайлом и доступен игроку")
    void shopIsPlacedOnMap() {
        Game game = new Game(DifficultyType.EASY);
        DungeonGenerator gen = game.getGenerator();

        Position shop = gen.getShopPosition();
        assertNotNull(shop, "Магазин должен появиться на уровне");
        assertTrue(gen.isPositionWalkable(shop), "На магазин нужно уметь зайти");
    }

    @RepeatedTest(20)
    @DisplayName("Магазин стоит в комнате, но не в стартовой и не на выходе")
    void shopIsNotInStartRoom() {
        Game game = new Game(DifficultyType.EASY);
        DungeonGenerator gen = game.getGenerator();
        Position shop = gen.getShopPosition();

        Room host = roomAt(gen, shop);
        assertNotNull(host, "Магазин должен стоять внутри комнаты");
        assertFalse(host == gen.getRooms().getFirst(), "Не стартовая комната");
        assertFalse(shop.equals(game.getPosLevel()), "Магазин не должен закрывать выход");
    }

    @RepeatedTest(10)
    @DisplayName("Магазин закрыт, пока игрок на него не встал")
    void shopClosedUntilPlayerStandsOnIt() {
        Game game = new Game(DifficultyType.EASY);
        assertFalse(game.isShopOpen(), "На старте игрок не на магазине, панель закрыта");
    }

    @RepeatedTest(10)
    @DisplayName("Тайл магазина переживает загрузку сохранения")
    void shopTileRestored() {
        Game game = new Game(DifficultyType.EASY);
        Position shop = game.getGenerator().getShopPosition();

        game.placeRestoredEntitiesOnMap();

        assertTrue(game.getGenerator().getMap()[shop.getX()][shop.getY()] == TileType.SHOP
                        || shop.equals(game.getPlayer().getPosition()),
                "После восстановления магазин должен снова быть на карте");
    }

    private Room roomAt(DungeonGenerator gen, Position pos) {
        for (Room room : gen.getRooms()) {
            Position rp = room.getPosition();
            if (pos.getX() >= rp.getX() && pos.getX() < rp.getX() + room.getWidth()
                    && pos.getY() >= rp.getY() && pos.getY() < rp.getY() + room.getHeight()) {
                return room;
            }
        }
        return null;
    }
}
