package domain.controller;

import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.gameSession.GameFacade;
import domain.gameSession.GameFacadeImpl;
import domain.navigator.Position;
import domain.navigator.DirectionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Controller interface.
 * Delegates to GameFacade and notifies listeners of events.
 */
public class ControllerImpl implements Controller, GameEventListener {

    private final GameFacade gameFacade;
    private final List<GameEventListener> listeners;
    private boolean isRunning;

    public ControllerImpl() {
        this.gameFacade = new GameFacadeImpl();
        this.listeners = new ArrayList<>();
        this.isRunning = false;
    }

    // ========== Game Control ==========

    @Override
    public void start() {
        if (!isRunning) {
            isRunning = true;
            gameFacade.startGame();
        }
    }

    @Override
    public void stop() {
        isRunning = false;
        gameFacade.setGameEnded(true);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    // ========== Game State Access ==========

    @Override
    public GameFacade getGameFacade() {
        return gameFacade;
    }

    @Override
    public Player getPlayer() {
        return gameFacade.getPlayer();
    }

    @Override
    public domain.backpack.Backpack getBackpack() {
        return gameFacade.getBackpack();
    }

    @Override
    public boolean isGameEnded() {
        return gameFacade.isGameEnded();
    }

    @Override
    public void setGameEnded(boolean ended) {
        gameFacade.setGameEnded(ended);
    }

    // ========== Player Movement ==========

    @Override
    public void movePlayer(DirectionType direction) {
        Position oldPos = getPlayerPosition();
        gameFacade.movePlayer(direction);
        Position newPos = getPlayerPosition();
        
        // Notify listeners if player moved
        if (!oldPos.equals(newPos)) {
            notifyListeners(listener -> listener.onPlayerMove(getPlayer(), newPos));
        }
    }

    @Override
    public Position getPlayerPosition() {
        return getPlayer().getPosition();
    }

    // ========== Inventory Management ==========

    @Override
    public boolean useItem(int index, ItemsType type) {
        boolean success = gameFacade.useItem(index, type);
        if (success) {
            Item item = getBackpack().getItemByIndex(index, type);
            if (item != null) {
                notifyListeners(listener -> listener.onItemUsed(getPlayer(), item, type));
            }
        }
        return success;
    }

    @Override
    public void selectInventoryType(ItemsType type) {
        gameFacade.selectInventoryType(type);
    }

    // ========== Enemy Access ==========

    @Override
    public List<Enemies> getAllEnemies() {
        return gameFacade.getAllEnemies();
    }

    @Override
    public Enemies getEnemyAtPosition(Position pos) {
        for (Enemies enemy : gameFacade.getAllEnemies()) {
            if (enemy.getHealth() > 0 && enemy.getPosition().equals(pos)) {
                return enemy;
            }
        }
        return null;
    }

    // ========== Event Listeners ==========

    @Override
    public void addListener(GameEventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void clearListeners() {
        listeners.clear();
    }

    // ========== Event Notification ==========

    private void notifyListeners(ListenerCallback callback) {
        for (GameEventListener listener : listeners) {
            try {
                callback.invoke(listener);
            } catch (Exception e) {
                // Log error but continue with other listeners
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }

    // ========== Event Handler Implementations ==========

    @Override
    public void onPlayerMove(Player player, Position newPos) {
        // Controller already handles this in movePlayer
    }

    @Override
    public void onPlayerAttack(Player player, Enemies enemy, int damage) {
        notifyListeners(listener -> listener.onPlayerAttack(player, enemy, damage));
    }

    @Override
    public void onEnemyAttack(Enemies enemy, Player player, int damage) {
        notifyListeners(listener -> listener.onEnemyAttack(enemy, player, damage));
    }

    @Override
    public void onItemCollected(Player player, Item item) {
        notifyListeners(listener -> listener.onItemCollected(player, item));
    }

    @Override
    public void onItemUsed(Player player, Item item, ItemsType type) {
        notifyListeners(listener -> listener.onItemUsed(player, item, type));
    }

    @Override
    public void onLevelChange(int level) {
        notifyListeners(listener -> listener.onLevelChange(level));
    }

    @Override
    public void onGameEnd(boolean win, int finalScore) {
        notifyListeners(listener -> listener.onGameEnd(win, finalScore));
    }

    @Override
    public void onEnemyDefeated(Enemies enemy) {
        notifyListeners(listener -> listener.onEnemyDefeated(enemy));
    }

    @Override
    public void onPlayerTakeDamage(Player player, int damage) {
        notifyListeners(listener -> listener.onPlayerTakeDamage(player, damage));
    }

    @Override
    public void onPlayerGainGold(Player player, int gold) {
        notifyListeners(listener -> listener.onPlayerGainGold(player, gold));
    }

    // Helper interface for listener callbacks
    @FunctionalInterface
    private interface ListenerCallback {
        void invoke(GameEventListener listener);
    }
}
