package domain.controller;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.Position;

/**
 * Listener interface for game events.
 * Controllers can implement this to react to game changes.
 */
public interface GameEventListener {

    /**
     * Called when player moves to a new position
     * @param player the player
     * @param newPos the new position
     */
    void onPlayerMove(Player player, Position newPos);

    /**
     * Called when player attacks an enemy
     * @param player the player
     * @param enemy the enemy being attacked
     * @param damage dealt
     */
    void onPlayerAttack(Player player, Enemies enemy, int damage);

    /**
     * Called when enemy attacks player
     * @param enemy the enemy
     * @param player the player
     * @param damage dealt
     */
    void onEnemyAttack(Enemies enemy, Player player, int damage);

    /**
     * Called when player collects an item
     * @param player the player
     * @param item the collected item
     */
    void onItemCollected(Player player, domain.backpack.Item item);

    /**
     * Called when player uses an item
     * @param player the player
     * @param item the used item
     * @param type item type
     */
    void onItemUsed(Player player, domain.backpack.Item item, domain.backpack.ItemsType type);

    /**
     * Called when level changes
     * @param level new level number
     */
    void onLevelChange(int level);

    /**
     * Called when game ends
     * @param win true if player won, false if lost
     * @param finalScore final score (gold)
     */
    void onGameEnd(boolean win, int finalScore);

    /**
     * Called when enemy is defeated
     * @param enemy the defeated enemy
     */
    void onEnemyDefeated(Enemies enemy);

    /**
     * Called when player takes damage
     * @param player the player
     * @param damage taken
     */
    void onPlayerTakeDamage(Player player, int damage);

    /**
     * Called when player gains gold
     * @param player the player
     * @param gold gained
     */
    void onPlayerGainGold(Player player, int gold);
}
