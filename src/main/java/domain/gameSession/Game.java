package domain.gameSession;

import domain.backpack.Backpack;
import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.battle.AttackSystem;
import domain.battle.BattleInfoType;

import static domain.battle.CharacterType.*;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.EnemiesType;
import domain.map.*;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Game {

    private final Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
    private DungeonGenerator generator;
    private List<Room> rooms;
    private List<Corridor> corridors;
    private Player player;
    private Backpack backpack;
    private List<Item> allItemList = new ArrayList<>();
    private final List<Enemies> allEnemiesList = new ArrayList<>();
    private final BattleInfoType battleInfo = new BattleInfoType();
    private final AttackSystem attackSystem = new AttackSystem();
    private int level = 1;
    private Position posLevel;
    private boolean isGameEnded = false;
    private ItemsType selectedInventoryType = null; // Тип предмета, выбранный для использования

    public Game() {
        generateNewLevel();

        if (rooms.isEmpty()) {
            throw new IllegalStateException("Failed to generate dungeon: no rooms created");
        }

        this.player = new Player(rooms.getFirst().getCentreRoom());
        this.backpack = new Backpack();

        initializeGame();
    }

    public DungeonGenerator getGenerator() {
        return generator;
    }
    public void setGenerator(DungeonGenerator generator) {this.generator = generator;}

    public List<Room> getRooms() {return rooms;}
    public void setRooms(List<Room> rooms) {this.rooms = rooms;}

    public void setBackpack(Backpack backpack) {this.backpack = backpack;}

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) { this.player = player; }

    public Backpack getBackpack() {
        return backpack;
    }

    public Position getPosLevel() {
        return posLevel;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        isGameEnded = gameEnded;
    }

    public List<Enemies> getAllEnemiesList() {return allEnemiesList;}

    public List<Item> getAllItemList() {
        return allItemList;
    }

    /**
     * Public method to use an item by index and type
     * @param index index of the item in backpack
     * @param type type of the item
     * @return true if item was used successfully
     */
    public boolean useItemByIndex(int index, ItemsType type) {
        return backpack.useItemByIndex(index, type, player);
    }

    /**
     * Public method to select inventory type for item usage
     * @param type type of items to select
     */
    public void selectInventoryType(ItemsType type) {
        selectedInventoryType = type;
        backpack.seeList(type);
        System.out.println("Введите цифру 1-9 для выбора предмета:");
    }


    /**
     * Генерирует новый уровень с новыми комнатами и коридорами
     */
    public void generateNewLevel() {
        if (level >= 25) {
            return;
        }
        Level.setLevelUp(level++);
        this.generator = new DungeonGenerator();
        this.generator.generateDungeon();
        this.rooms = generator.getRooms();
        this.corridors = generator.getCorridors();
        allEnemiesList.clear();
        allItemList.clear();
    }

    /**
     * метод инициализирует игру
     */
    public void initializeGame() {
        generator.createPlayer(player);
        for (var room : rooms) {
            if (room != rooms.getFirst()) {
                allItemList.addAll(room.getItemList());
                allEnemiesList.addAll(room.getEnemyList());
                generator.createItem(room);
                generator.createEnemies(room);
            }
            if (room == rooms.getLast()) {
                posLevel = generator.createLevel(room);
            }
        }
    }

    /**
     * метод запускает игру
     */
    public void start() {
        while (player.getHealth() > 0 && !isGameEnded) {
            generator.printMap();
            System.out.println(player + " lvl: " + Level.getLevelUp());

            String input = scanner.nextLine().toLowerCase().trim();
            handleInput(input);
            // Ход врагов
            if (player.getHealth() > 0 && !isGameEnded) {
                enemyTurns();
            }
        }
        if (!isGameEnded) {
            printGameOver();
        }
    }

    private void handleInput(String input) {
        DirectionType direction = parseDirection(input);
        if (direction != null) {
            movePlayer(direction);
        } else {
            handleInventoryCommand(input);
        }
    }

    private DirectionType parseDirection(String input) {
        // Если выбран тип предмета, цифры используются для выбора предмета
        if (selectedInventoryType != null) {
            if (input.length() == 1) {
                char c = input.charAt(0);
                if (c >= '1' && c <= '9') {
                    int index = c - '1'; // 1 -> 0, 2 -> 1, ...
                    if (backpack.useItemByIndex(index, selectedInventoryType, player)) {
                        System.out.println("Предмет использован!");
                    } else {
                        System.out.println("Предмет с этим индексом не найден!");
                    }
                    selectedInventoryType = null; // Сброс выбора
                    return null;
                }
            }
        }
        return switch (input) {
            case "w" -> DirectionType.FORWARD;
            case "s" -> DirectionType.DOWN;
            case "a" -> DirectionType.LEFT;
            case "d" -> DirectionType.RIGHT;
            default -> null;
        };
    }

    /**
     * Move player in specified direction
     * @param direction direction to move
     */
    public void movePlayer(DirectionType direction) {
        // Если игрок в стане, пропускаем ход
        if (player.getIsStunned()) {
            player.setIsStunned(false);
            return;
        }

        Position nextPos = direction.applyTo(player.getPosition());

        Enemies targetEnemy = getEnemyAtPosition(nextPos);
        if (targetEnemy != null && targetEnemy.getHealth() > 0) {
            attackEnemy(targetEnemy);
            return;
        }

        if (!generator.isPositionWalkable(nextPos)) {
            return;
        }

        // Проверка перехода на следующий уровень (проверяем следующую позицию)
        if (nextPos.equals(posLevel)) {
            // Если это 25-й уровень - победа
            if (level >= 25) {
                winGame();
                isGameEnded = true;
                return;
            }
            generateNewLevel();
            player.setPosition(rooms.getFirst().getCentreRoom());
            initializeGame();
            return;
        }

        generator.deletePosPlayer(player);
        player.setPosition(nextPos);
        checkAndCollectItems();
        generator.createPlayer(player);
    }

    /**
     * Получает врага, находящегося на указанной позиции
     * @param pos позиция для проверки
     * @return враг или null, если на позиции нет врага
     */
    private Enemies getEnemyAtPosition(Position pos) {
        for (Enemies enemy : allEnemiesList) {
            if (enemy.getHealth() > 0 && enemy.getPosition().equals(pos)) {
                return enemy;
            }
        }
        return null;
    }

    /**
     * Атакует указанного врага
     * @param enemy враг для атаки
     */
    private void attackEnemy(Enemies enemy) {
        System.out.println("Атака врага: " + enemy.getType());
        attackSystem.attack(player, enemy, PLAYER, battleInfo, backpack);
    }

    /**
     * метод проверки предмета на карте с позицией игрока
     */
    private void checkAndCollectItems() {
        for (var item : allItemList) {
            if (player.getPosition().equals(item.getPosition())) {
                backpack.takeItem(item);
            }
        }
    }

    private void handleInventoryCommand(String input) {
        // Если выбран тип предмета, то цифра 1-9 используется для выбора предмета
        if (selectedInventoryType != null) {
            if (input.length() == 1) {
                char c = input.charAt(0);
                if (c >= '1' && c <= '9') {
                    int index = c - '1'; // 1 -> 0, 2 -> 1, ...
                    if (backpack.useItemByIndex(index, selectedInventoryType, player)) {
                        System.out.println("Предмет использован!");
                    } else {
                        System.out.println("Предмет с этим индексом не найден!");
                    }
                    selectedInventoryType = null; // Сброс выбора
                    return;
                }
            }
            // Если не цифра, сброс выбора
            selectedInventoryType = null;
        }
        
        // Проверяем, является ли команда командой выбора типа предмета (e, h, j, k)
        if (handleInventoryTypeSelection(input)) {
            return;
        }
        
        // Проверяем, является ли команда командой использования предмета (например: e0, h1, j2, k3)
        if (input.length() >= 2) {
            String typeChar = input.substring(0, 1);
            try {
                int index = Integer.parseInt(input.substring(1));
                ItemsType type = parseInventoryType(typeChar);
                if (type != null) {
                    if (backpack.useItemByIndex(index, type, player)) {
                        System.out.println("Предмет использован!");
                    } else {
                        System.out.println("Неверный индекс предмета!");
                    }
                    return;
                }
            } catch (NumberFormatException e) {
                // Не число, продолжаем как обычную команду
            }
        }
        
        // Обычное открытие инвентаря
        ItemsType type = parseInventoryType(input);
        if (type != null) {
            backpack.seeList(type);
        }
    }

    private ItemsType parseInventoryType(String input) {
        return switch (input) {
            case "e" -> ItemsType.ELIXIR;
            case "h" -> ItemsType.SCROLL;
            case "j" -> ItemsType.FOOD;
            case "k" -> ItemsType.WEAPON;
            default -> null;
        };
    }

    /**
     * Обрабатывает выбор типа предмета (e, h, j, k)
     * @param input ввод пользователя
     * @return true если был выбран тип предмета
     */
    private boolean handleInventoryTypeSelection(String input) {
        ItemsType type = parseInventoryType(input);
        if (type != null) {
            selectedInventoryType = type;
            backpack.seeList(type);
            System.out.println("Введите цифру 1-9 для выбора предмета:");
            return true;
        }
        return false;
    }

    /**
     * Ход врагов - каждый враг делает движение и атаку
     */
    private void enemyTurns() {
        for (Enemies enemy : allEnemiesList) {
            if (enemy.getHealth() > 0) {
                DirectionType moveDir = enemy.decideMove(player);

                if (moveDir != null) {
                    Position nextEnemyPos = moveDir.applyTo(enemy.getPosition());

                    // Проверяем, не занята ли позиция игроком, другим врагом, стеной, предметом или переходом на след уровень
                    if (
                            !isPositionOccupied(nextEnemyPos) &&
                            generator.isPositionWalkable(nextEnemyPos) &&
                            !nextEnemyPos.equals(posLevel)
                    ) {
                        // Проверяем, что на позиции нет предмета
                        TileType tileType = generator.getMap()[nextEnemyPos.getX()][nextEnemyPos.getY()];
                        if (
                                tileType != TileType.ELIXIR &&
                                tileType != TileType.SCROLL &&
                                tileType != TileType.WEAPON &&
                                tileType != TileType.FOOD
                        ) {
                            generator.deleteEnemy(enemy);
                            enemy.setPosition(nextEnemyPos);
                            generator.createEnemy(enemy);
                        }
                    }
                }

                // Атака игрока (только если враг был на соседней клетке ДО движения)
                if (player.getPosition().distanceTo(enemy.getPosition()) < 2) {
                    // Сброс флага первой атаки вампира при начале боя
                    if (enemy.getType() == EnemiesType.VAMPIRE) {
                        battleInfo.vampireFirstAttack = true;
                    }
                    attackSystem.attack(player, enemy, ENEMIES, battleInfo, backpack);
                }
            }
        }
    }

    /**
     * Проверяет, занята ли позиция другим объектом
     */
    private boolean isPositionOccupied(Position pos) {
        // Проверяем игрока
        if (pos.equals(player.getPosition())) {
            return true;
        }
        // Проверяем других врагов
        for (Enemies enemy : allEnemiesList) {
            if (enemy.getHealth() > 0 && pos.equals(enemy.getPosition())) {
                return true;
            }
        }

        return false;
    }

    private void printGameOver() {
        System.out.println("\n=== GAME OVER ===");
        System.out.println("Final score: " + player.getGold() + " gold");
    }

    private void winGame() {
        System.out.println("\n=== You win! ===");
        System.out.println("Final score: " + player.getGold() + " gold");
    }
}
