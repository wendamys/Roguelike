package domain.gameSession;

import domain.backpack.Backpack;
import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.map.DungeonGenerator;
import domain.navigator.Position;
import domain.navigator.DirectionType;

import java.util.List;

/**
 * GameFacade provides a simplified interface to the Game class.
 * It acts as a mediator between the Game and external controllers.
 */
public interface GameFacade {

    // ============= Game State =============

    boolean isGameEnded();

    void setGameEnded(boolean ended);

    // ============= Access to Core Components =============

    Player getPlayer();

    Backpack getBackpack();

    DungeonGenerator getGenerator();

    List<Enemies> getAllEnemies();

    List<Item> getAllItems();

    Position getLevelPosition();

    // ============= Player Movement =============

    void movePlayer(DirectionType direction);

    // ============= Inventory Management =============

    /**
     * Use an item by index and type
     * @param index index of the item
     * @param type type of the item
     * @return true if item was used successfully
     */
    boolean useItem(int index, ItemsType type);

    void selectInventoryType(ItemsType type);

    // ============= Game Control =============

    void startGame();

    void nextLevel();
}
