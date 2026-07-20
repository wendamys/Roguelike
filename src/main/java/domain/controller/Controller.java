package domain.controller;

import domain.backpack.ItemsType;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.gameSession.GameFacade;
import domain.navigator.Position;
import domain.navigator.DirectionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller acts as a mediator between the view and the game logic.
 * It uses GameFacade to interact with the game and notifies listeners of events.
 */
public interface Controller {

    // ========== Game Control ==========

    /**
     * Start the game
     */
    void start();

    /**
     * Stop the game
     */
    void stop();

    /**
     * Check if game is running
     * @return true if game is running
     */
    boolean isRunning();

    // ========== Game State Access ==========

    /**
     * Get the game facade instance
     * @return game facade
     */
    GameFacade getGameFacade();

    /**
     * Get the player instance
     * @return player
     */
    Player getPlayer();

    /**
     * Get the backpack instance
     * @return backpack
     */
    domain.backpack.Backpack getBackpack();

    /**
     * Check if game is ended
     * @return true if game ended
     */
    boolean isGameEnded();

    /**
     * Set game ended status
     * @param ended true if game ended
     */
    void setGameEnded(boolean ended);

    // ========== Player Movement ==========

    /**
     * Move player in specified direction
     * @param direction direction to move
     */
    void movePlayer(DirectionType direction);

    /**
     * Get player position
     * @return player position
     */
    Position getPlayerPosition();

    // ========== Inventory Management ==========

    /**
     * Use an item by index and type
     * @param index index of the item
     * @param type type of the item
     * @return true if item was used successfully
     */
    boolean useItem(int index, ItemsType type);

    /**
     * Select inventory type for item usage
     * @param type type of items to select
     */
    void selectInventoryType(ItemsType type);

    // ========== Enemy Access ==========

    /**
     * Get all enemies
     * @return list of enemies
     */
    List<Enemies> getAllEnemies();

    /**
     * Get enemies at specific position
     * @param pos position to check
     * @return enemy at position or null
     */
    Enemies getEnemyAtPosition(Position pos);

    // ========== Event Listeners ==========

    /**
     * Add a listener for game events
     * @param listener the listener to add
     */
    void addListener(GameEventListener listener);

    /**
     * Remove a listener
     * @param listener the listener to remove
     */
    void removeListener(GameEventListener listener);

    /**
     * Remove all listeners
     */
    void clearListeners();
}
