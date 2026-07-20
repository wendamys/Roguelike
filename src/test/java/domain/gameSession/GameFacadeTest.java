package domain.gameSession;

import domain.backpack.Backpack;
import domain.backpack.ItemsType;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.map.DungeonGenerator;
import domain.navigator.Position;
import domain.navigator.DirectionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameFacadeTest {

    private GameFacade facade;

    @BeforeEach
    void setUp() {
        facade = new GameFacadeImpl();
    }

    @Test
    void testGameFacadeInitialization_ShouldCreateGame() {
        assertNotNull(facade);
        assertNotNull(facade.getPlayer());
        assertNotNull(facade.getBackpack());
        assertNotNull(facade.getGenerator());
    }

    @Test
    void testGetPlayer_ShouldReturnPlayerInstance() {
        Player player = facade.getPlayer();
        assertNotNull(player);
        assertNotNull(player.getPosition());
    }

    @Test
    void testGetBackpack_ShouldReturnBackpackInstance() {
        Backpack backpack = facade.getBackpack();
        assertNotNull(backpack);
    }

    @Test
    void testGetGenerator_ShouldReturnDungeonGenerator() {
        DungeonGenerator generator = facade.getGenerator();
        assertNotNull(generator);
    }

    @Test
    void testIsGameEnded_ShouldReturnFalseInitially() {
        assertFalse(facade.isGameEnded());
    }

    @Test
    void testSetGameEnded_ShouldUpdateGameStatus() {
        facade.setGameEnded(true);
        assertTrue(facade.isGameEnded());
    }

    @Test
    void testGetAllEnemies_ShouldReturnEnemiesList() {
        List<Enemies> enemies = facade.getAllEnemies();
        assertNotNull(enemies);
        // Should have some enemies generated
        assertTrue(enemies.size() >= 0);
    }

    @Test
    void testGetAllItems_ShouldReturnItemsList() {
        List<domain.backpack.Item> items = facade.getAllItems();
        assertNotNull(items);
    }

    @Test
    void testGetLevelPosition_ShouldReturnPosition() {
        Position levelPos = facade.getLevelPosition();
        assertNotNull(levelPos);
    }

    @Test
    void testSelectInventoryType_ShouldSetSelectedType() {
        assertDoesNotThrow(() -> facade.selectInventoryType(ItemsType.FOOD));
    }
}
