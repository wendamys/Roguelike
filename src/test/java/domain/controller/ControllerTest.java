package domain.controller;

import domain.backpack.ItemsType;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.Position;
import domain.navigator.DirectionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private Controller controller;

    @BeforeEach
    void setUp() {
        controller = new ControllerImpl();
    }

    @Test
    void testControllerInitialization_ShouldCreateGame() {
        assertNotNull(controller);
        assertNotNull(controller.getGameFacade());
        assertNotNull(controller.getPlayer());
    }

    @Test
    void testGetGameFacade_ShouldReturnGameFacade() {
        assertNotNull(controller.getGameFacade());
    }

    @Test
    void testGetPlayer_ShouldReturnPlayer() {
        Player player = controller.getPlayer();
        assertNotNull(player);
        assertNotNull(player.getPosition());
    }

    @Test
    void testGetBackpack_ShouldReturnBackpack() {
        assertNotNull(controller.getBackpack());
    }

    @Test
    void testIsGameEnded_ShouldReturnFalseInitially() {
        assertFalse(controller.isGameEnded());
    }

    @Test
    void testSetGameEnded_ShouldUpdateGameStatus() {
        controller.setGameEnded(true);
        assertTrue(controller.isGameEnded());
    }

    @Test
    void testGetPlayerPosition_ShouldReturnPosition() {
        Position pos = controller.getPlayerPosition();
        assertNotNull(pos);
    }

    @Test
    void testGetAllEnemies_ShouldReturnEnemiesList() {
        List<Enemies> enemies = controller.getAllEnemies();
        assertNotNull(enemies);
    }

    @Test
    void testMovePlayer_ShouldNotThrowException() {
        assertDoesNotThrow(() -> controller.movePlayer(DirectionType.FORWARD));
    }

    @Test
    void testSelectInventoryType_ShouldNotThrowException() {
        assertDoesNotThrow(() -> controller.selectInventoryType(ItemsType.FOOD));
    }

    @Test
    void testAddListener_ShouldAddListener() {
        GameEventListener listener = new GameEventListener() {
            @Override
            public void onPlayerMove(Player player, Position newPos) {}
            @Override
            public void onPlayerAttack(Player player, Enemies enemy, int damage) {}
            @Override
            public void onEnemyAttack(Enemies enemy, Player player, int damage) {}
            @Override
            public void onItemCollected(Player player, domain.backpack.Item item) {}
            @Override
            public void onItemUsed(Player player, domain.backpack.Item item, domain.backpack.ItemsType type) {}
            @Override
            public void onLevelChange(int level) {}
            @Override
            public void onGameEnd(boolean win, int finalScore) {}
            @Override
            public void onEnemyDefeated(Enemies enemy) {}
            @Override
            public void onPlayerTakeDamage(Player player, int damage) {}
            @Override
            public void onPlayerGainGold(Player player, int gold) {}
        };
        controller.addListener(listener);
    }

    @Test
    void testRemoveListener_ShouldNotThrowException() {
        GameEventListener listener = new GameEventListener() {
            @Override
            public void onPlayerMove(Player player, Position newPos) {}
            @Override
            public void onPlayerAttack(Player player, Enemies enemy, int damage) {}
            @Override
            public void onEnemyAttack(Enemies enemy, Player player, int damage) {}
            @Override
            public void onItemCollected(Player player, domain.backpack.Item item) {}
            @Override
            public void onItemUsed(Player player, domain.backpack.Item item, domain.backpack.ItemsType type) {}
            @Override
            public void onLevelChange(int level) {}
            @Override
            public void onGameEnd(boolean win, int finalScore) {}
            @Override
            public void onEnemyDefeated(Enemies enemy) {}
            @Override
            public void onPlayerTakeDamage(Player player, int damage) {}
            @Override
            public void onPlayerGainGold(Player player, int gold) {}
        };
        controller.addListener(listener);
        controller.removeListener(listener);
    }

    @Test
    void testClearListeners_ShouldNotThrowException() {
        assertDoesNotThrow(() -> controller.clearListeners());
    }
}
