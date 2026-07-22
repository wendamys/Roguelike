package domain.gameSession;

import domain.backpack.Backpack;
import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.battle.AttackSystem;
import domain.battle.BattleInfoType;
import domain.characters.enemies.Mimic;

import static domain.battle.CharacterType.*;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.EnemiesType;
import domain.map.*;
import domain.navigator.DirectionType;
import domain.navigator.Position;
import domain.shop.Shop;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Predicate;

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
    private DifficultyType difficulty;
    private FogOfWar fog;
    private int enemiesKilled = 0;
    private Shop shop;
    private boolean shopOpen = false;
    private ItemsType selectedInventoryType = null; // Тип предмета, выбранный для использования
    private static final int MESSAGE_LOG_CAPACITY = 10;
    private final Deque<String> messageLog = new ArrayDeque<>(); // История последних сообщений, для presentation-слоя

    public Game() {
        this(DifficultyType.EASY);
    }

    public Game(DifficultyType difficulty) {
        this.difficulty = difficulty;
        generateNewLevel();

        if (rooms.isEmpty()) {
            throw new IllegalStateException("Failed to generate dungeon: no rooms created");
        }

        this.player = new Player(rooms.getFirst().getCentreRoom());
        this.backpack = new Backpack();

        initializeGame();
    }

    public DifficultyType getDifficulty() {
        return difficulty;
    }

    public FogOfWar getFog() {
        return fog;
    }

    public Shop getShop() {
        return shop;
    }

    public boolean isShopOpen() {
        return shopOpen;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public void setEnemiesKilled(int enemiesKilled) {
        this.enemiesKilled = enemiesKilled;
    }

    /**
     * метод считает итоговый счёт игрока
     * @return счёт с учётом золота, убитых врагов, достигнутого уровня и сложности
     */
    public int calculateScore() {
        int base = player.getGold() + enemiesKilled * 10 + (Level.getLevelUp() - 1) * 50;
        // округляем, а не обрезаем: 180 * 1.15 в double даёт 206.9999, обрезание дало бы 206
        return Math.round((float) (base * difficulty.getCoef()));
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

    public void setPosLevel(Position posLevel) {
        this.posLevel = posLevel;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public ItemsType getSelectedInventoryType() {
        return selectedInventoryType;
    }

    /**
     * метод добавляет сообщение в лог, отбрасывая самое старое при переполнении
     * @param message игровое сообщение
     */
    private void addMessage(String message) {
        messageLog.addLast(message);
        if (messageLog.size() > MESSAGE_LOG_CAPACITY) {
            messageLog.removeFirst();
        }
    }

    /**
     * метод возвращает историю сообщений от старых к новым
     * @return список сообщений, не более MESSAGE_LOG_CAPACITY
     */
    public List<String> getMessageLog() {
        return new ArrayList<>(messageLog);
    }

    /**
     * метод возвращает вместимость лога, нужен presentation-слою
     * для расчёта позиции панели логов
     * @return максимальное число сообщений в логе
     */
    public static int getMessageLogCapacity() {
        return MESSAGE_LOG_CAPACITY;
    }


    /**
     * Генерирует новый уровень с новыми комнатами и коридорами
     */
    private void generateNewLevel() {
        if (level >= 25) {
            return;
        }
        Level.setLevelUp(level++);
        this.generator = new DungeonGenerator(difficulty);
        this.generator.generateDungeon();
        this.rooms = generator.getRooms();
        this.corridors = generator.getCorridors();
        this.fog = new FogOfWar(generator.getMapWidth(), generator.getMapHeight());
        this.shop = new Shop(difficulty);
        allEnemiesList.clear();
        allItemList.clear();
    }

    /**
     * метод инициализирует игру
     */
    private void initializeGame() {
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
        generator.placeShop();
        fog.update(player, rooms, difficulty, generator.getMap());
    }

    /**
     * метод восстанавливает плоские списки предметов и врагов по комнатам
     * (используется при загрузке — комнаты уже содержат восстановленные списки)
     */
    public void restoreItemsAndEnemies() {
        allItemList.clear();
        allEnemiesList.clear();
        for (Room room : rooms) {
            if (room != rooms.getFirst()) {
                allItemList.addAll(room.getItemList());
                allEnemiesList.addAll(room.getEnemyList());
            }
        }
    }

    /**
     * метод расставляет игрока, предметы, врагов и выход на восстановленной карте
     * (используется при загрузке, не добавляет предметы/врагов повторно в комнаты)
     */
    public void placeRestoredEntitiesOnMap() {
        // rebuildMap рисует только пол и стены, двери с ключами возвращает генератор
        generator.redrawStaticEntities();

        generator.createPlayer(player);
        for (Room room : rooms) {
            if (room != rooms.getFirst()) {
                generator.createItem(room);
                generator.createEnemies(room);
            }
            if (room == rooms.getLast()) {
                posLevel = generator.createLevel(room);
            }
        }

        // старые сохранения не знали про магазин - ставим его заново, иначе уровень будет без него
        if (generator.getShopPosition() == null) {
            generator.placeShop();
        }
        // состояние панели производное от позиции игрока, отдельно его не храним
        shopOpen = player.getPosition().equals(generator.getShopPosition());

        fog.update(player, rooms, difficulty, generator.getMap());
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

    /**
     * метод обрабатывает один ход, вызывается из presentation (Lanterna),
     * не печатает ничего в консоль — вся отрисовка выполняется в presentation-слое
     * @param input команда игрока (направление или команда инвентаря)
     */
    public void processInput(String input) {
        handleInput(input);
        fog.update(player, rooms, difficulty, generator.getMap());
        if (player.getHealth() > 0 && !isGameEnded) {
            enemyTurns();
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
                    backpack.useItemByIndex(index, selectedInventoryType, player);
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

    private void movePlayer(DirectionType direction) {
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

        // Шаг в дверь: с ключом она открывается, без ключа ход просто тратится
        if (tryOpenDoor(nextPos)) {
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
        // игрок затирал собой тайл магазина, возвращаем его на место
        if (player.getPosition().equals(generator.getShopPosition())) {
            generator.drawShop();
        }

        player.setPosition(nextPos);
        checkAndCollectItems();
        checkAndCollectKeys();
        generator.createPlayer(player);

        // магазин открыт, пока игрок стоит на его клетке
        shopOpen = nextPos.equals(generator.getShopPosition());
    }

    /**
     * метод обрабатывает шаг в дверь
     * @param pos клетка, куда шагает игрок
     * @return true если это была дверь и ход потрачен
     */
    private boolean tryOpenDoor(Position pos) {
        if (!generator.isInBounds(pos.getX(), pos.getY())) {
            return false;
        }
        TileType tile = generator.getMap()[pos.getX()][pos.getY()];
        ColorKey color = DungeonGenerator.colorOfDoorTile(tile);
        if (color == null) {
            return false;
        }

        if (player.hasKey(color)) {
            unlockRoom(color);
            addMessage("Открыл " + colorLabel(color) + " дверь");
        } else {
            addMessage("Дверь заперта (" + colorLabel(color) + ")");
        }
        return true;
    }

    /**
     * метод отпирает комнату целиком: один ключ открывает все её входы сразу,
     * иначе закрытую дверь можно было бы обойти через соседний проход
     * @param color цвет ключа
     */
    private void unlockRoom(ColorKey color) {
        for (Room room : rooms) {
            Door door = room.getDoor();
            if (door == null || door.getColorKey() != color || !door.getIsClose()) {
                continue;
            }
            for (Position entrance : door.getEntrances()) {
                generator.getMap()[entrance.getX()][entrance.getY()] = TileType.FLOOR;
            }
            door.setClose(false);
        }
    }

    /**
     * метод подбирает ключ, если игрок встал на его клетку
     */
    private void checkAndCollectKeys() {
        generator.getKeys().removeIf(key -> {
            if (player.getPosition().equals(key.getPosition())) {
                player.addKey(key.getColorKey());
                addMessage("Подобрал " + colorLabel(key.getColorKey()) + " ключ");
                return true;
            }
            return false;
        });
    }

    /**
     * метод возвращает название цвета для сообщений в логе
     */
    private String colorLabel(ColorKey color) {
        return switch (color) {
            case GREEN -> "зелёный";
            case BLUE -> "синий";
            case RED -> "красный";
            case YELLOW -> "жёлтый";
        };
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
     * Атакует указанного врага и обновляет карту, если это мимик и он раскрылся
     * @param enemy враг для атаки
     */
    private void attackEnemy(Enemies enemy) {
        boolean wasAlive = enemy.getHealth() > 0;
        attackSystem.attack(player, enemy, PLAYER, battleInfo, backpack);
        drainBattleEvents();

        // считаем только переход из живого в мёртвого, чтобы добивание не накручивало счётчик
        if (wasAlive && enemy.getHealth() <= 0) {
            enemiesKilled++;
        }

        // Если мимик раскрылся, обновляем его отображение на карте
        if (enemy instanceof Mimic && !((Mimic) enemy).getAmbushAI().isMimicking()) {
            generator.deleteEnemy(enemy);
            generator.createEnemy(enemy);
        }
    }

    /**
     * метод проверки предмета на карте с позицией игрока
     */
    private void checkAndCollectItems() {
        allItemList.removeIf(item -> {
            if (player.getPosition().equals(item.getPosition())) {
                backpack.takeItem(item);
                return true;
            }
            return false;
        });
    }

    private void handleInventoryCommand(String input) {
        // При открытом магазине цифры пока не покупают - логика покупки не реализована
        if (shopOpen && input.length() == 1) {
            char c = input.charAt(0);
            if (c >= '1' && c <= '9') {
                addMessage("Покупка появится позже");
                return;
            }
        }

        // Если выбран тип предмета, то цифра 1-9 используется для выбора предмета
        if (selectedInventoryType != null) {
            if (input.length() == 1) {
                char c = input.charAt(0);
                if (c >= '1' && c <= '9') {
                    int index = c - '1'; // 1 -> 0, 2 -> 1, ...
                    backpack.useItemByIndex(index, selectedInventoryType, player);
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
                    backpack.useItemByIndex(index, type, player);
                    return;
                }
            } catch (NumberFormatException e) {
                // Не число, продолжаем как обычную команду
            }
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
            // подсказка про 1-9 постоянно висит в панели инвентаря, в лог её не дублируем
            selectedInventoryType = type;
            return true;
        }
        return false;
    }

    /**
     * Ход врагов - каждый враг делает движение и атаку
     */
    private void enemyTurns() {
        // Порядок условий важен: isPositionWalkable проверяет границы карты,
        // без него обращение к getMap() уйдёт за пределы массива на краю карты
        Predicate<Position> walkable = pos ->
                generator.isPositionWalkable(pos)
                && !isPositionOccupied(pos)
                && !pos.equals(posLevel)
                && !pos.equals(generator.getShopPosition())
                && !DungeonGenerator.isPickupTile(generator.getMap()[pos.getX()][pos.getY()]);

        for (Enemies enemy : allEnemiesList) {
            if (enemy.getHealth() <= 0) {
                continue;
            }

            // AI уже вернул заведомо проходимое направление, повторно проверять не нужно
            DirectionType moveDir = enemy.decideMove(player, walkable);
            if (moveDir != null) {
                generator.deleteEnemy(enemy);
                enemy.setPosition(moveDir.applyTo(enemy.getPosition()));
                generator.createEnemy(enemy);
            }

            // Атака игрока только ортогонально - оттуда, откуда он может ответить
            if (enemy.canAttack(player)) {
                // Сброс флага первой атаки вампира при начале боя
                if (enemy.getType() == EnemiesType.VAMPIRE) {
                    battleInfo.vampireFirstAttack = true;
                }
                attackSystem.attack(player, enemy, ENEMIES, battleInfo, backpack);
                drainBattleEvents();
            }
        }
    }

    /**
     * метод переливает события боя в лог сообщений и очищает их
     */
    private void drainBattleEvents() {
        for (String event : battleInfo.getEvents()) {
            addMessage(event);
        }
        battleInfo.getEvents().clear();
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
