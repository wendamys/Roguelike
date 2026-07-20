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
 * Implementation of GameFacade.
 * Delegates all operations to the internal Game instance.
 */
public class GameFacadeImpl implements GameFacade {

    private final Game game;

    public GameFacadeImpl() {
        this.game = new Game();
    }

    // ============= Game State =============

    @Override
    public boolean isGameEnded() {
        return game.isGameEnded();
    }

    @Override
    public void setGameEnded(boolean ended) {
        game.setGameEnded(ended);
    }

    // ============= Access to Core Components =============

    @Override
    public Player getPlayer() {
        return game.getPlayer();
    }

    @Override
    public Backpack getBackpack() {
        return game.getBackpack();
    }

    @Override
    public DungeonGenerator getGenerator() {
        return game.getGenerator();
    }

    @Override
    public List<Enemies> getAllEnemies() {
        return game.getAllEnemiesList();
    }

    @Override
    public List<Item> getAllItems() {
        return game.getAllItemList();
    }

    @Override
    public Position getLevelPosition() {
        return game.getPosLevel();
    }

    // ============= Player Movement =============

    @Override
    public void movePlayer(DirectionType direction) {
        game.movePlayer(direction);
    }

    // ============= Inventory Management =============

    /**
     * Use an item by index and type
     * @param index index of the item
     * @param type type of the item
     * @return true if item was used successfully
     */
    public boolean useItem(int index, ItemsType type) {
        return game.useItemByIndex(index, type);
    }

    @Override
    public void selectInventoryType(ItemsType type) {
        game.selectInventoryType(type);
    }

    // ============= Game Control =============

    @Override
    public void startGame() {
        game.start();
    }

    @Override
    public void nextLevel() {
        game.generateNewLevel();
        game.initializeGame();
    }
}
