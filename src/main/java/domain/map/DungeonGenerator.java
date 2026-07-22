package domain.map;

import domain.backpack.Item;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.EnemiesType;
import domain.characters.enemies.Mimic;
import domain.gameSession.DifficultyType;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DungeonGenerator {
    private static final int MAP_SIZE = 45;
    private static final int ROOM_PADDING = 5; // Отступ между комнатами

    private final int mapWidth = 70;
    private final int mapHeight = 60;
    private TileType[][] map;
    private List<Room> rooms;
    private List<Corridor> corridors;
    private final DifficultyType difficulty;
    private final List<Key> keys = new ArrayList<>();
    private final List<Room> lockedRooms = new ArrayList<>();

    public DungeonGenerator() {
        this(DifficultyType.EASY);
    }

    public DungeonGenerator(DifficultyType difficulty) {
        this.difficulty = difficulty;
        this.map = new TileType[mapWidth][mapHeight];
        this.rooms = new ArrayList<>();
        this.corridors = new ArrayList<>();
        initializeMap();
    }

    public TileType[][] getMap() {
        return map;
    }
    public void setMap(TileType[][] map) {
        this.map = map;
    }

    public List<Room> getRooms() {
        return rooms;
    }
    public void setRooms(List<Room> rooms) {this.rooms = rooms;}

    public List<Corridor> getCorridors() {
        return corridors;
    }
    public void setCorridors(List<Corridor> corridors) {this.corridors = corridors;}

    public List<Key> getKeys() {
        return keys;
    }

    public List<Room> getLockedRooms() {
        return lockedRooms;
    }

    public int getMapWidth() {
        return mapWidth;
    }
    public int getMapHeight() {
        return mapHeight;
    }

    /**
     * метод инициализирует карту стенами
     */
    private void initializeMap() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                map[x][y] = TileType.WALL;
            }
        }
    }

    /**
     * метод генерирует полное подземелье
     */
    public void generateDungeon() {
        generateRooms();
        buildCorridors();
        placeDoorsAndKeys();
    }

    /**
     * метод запирает часть комнат цветными дверями и раскладывает ключи цепочкой:
     * ключ первой запертой комнаты лежит в стартовой, ключ следующей - внутри предыдущей.
     * Так уровень гарантированно проходится.
     */
    private void placeDoorsAndKeys() {
        keys.clear();
        lockedRooms.clear();
        if (rooms.size() < 2) {
            return;
        }

        Room startRoom = rooms.getFirst();
        Position start = startRoom.getCentreRoom();
        Set<ColorKey> held = new HashSet<>();
        Room holder = startRoom;

        for (ColorKey color : ColorKey.values()) {
            Room locked = tryLockAnyRoom(color, start, held, holder);
            if (locked == null) {
                break;
            }
            // игрок гарантированно возьмёт этот ключ, значит комната снова доступна
            held.add(color);
            holder = locked;
        }
    }

    /**
     * метод пытается запереть одну из подходящих комнат данным цветом.
     * Запирание примеряется и откатывается, если ломает проходимость уровня:
     * двери могут разрезать коридор, который вёл к ранее запертой комнате.
     * @return запертая комната или null, если ни одну запереть не удалось
     */
    private Room tryLockAnyRoom(ColorKey color, Position start, Set<ColorKey> held, Room holder) {
        Set<Position> zone = reachable(start, held);

        for (Room candidate : rooms) {
            if (candidate == rooms.getFirst() || candidate.getDoor() != null) {
                continue;
            }
            if (!zone.contains(candidate.getCentreRoom())) {
                continue;
            }
            List<Position> entrances = findEntrances(candidate);
            if (entrances.isEmpty()) {
                continue;
            }
            // ключ кладём в уже доступную зону ДО того, как запрём комнату
            Position keyPos = freePositionInside(holder, zone);
            if (keyPos == null) {
                continue;
            }

            TileType keyBackup = map[keyPos.getX()][keyPos.getY()];
            List<TileType> doorBackup = new ArrayList<>();
            for (Position entrance : entrances) {
                doorBackup.add(map[entrance.getX()][entrance.getY()]);
            }

            Key key = new Key(keyPos, color);
            map[keyPos.getX()][keyPos.getY()] = keyTileFor(color);
            keys.add(key);
            for (Position entrance : entrances) {
                map[entrance.getX()][entrance.getY()] = doorTileFor(color);
            }
            candidate.setDoor(new Door(candidate, color, entrances));
            lockedRooms.add(candidate);

            if (levelIsSolvable(start)) {
                return candidate;
            }

            // откат: этот вариант сделал уровень непроходимым
            map[keyPos.getX()][keyPos.getY()] = keyBackup;
            for (int i = 0; i < entrances.size(); i++) {
                Position entrance = entrances.get(i);
                map[entrance.getX()][entrance.getY()] = doorBackup.get(i);
            }
            keys.remove(key);
            lockedRooms.remove(candidate);
            candidate.setDoor(null);
        }
        return null;
    }

    /**
     * метод проверяет, что уровень проходится: все ключи собираются по цепочке,
     * все запертые комнаты открываются и выход на следующий уровень достижим
     * @param start стартовая позиция игрока
     * @return true если уровень проходим
     */
    private boolean levelIsSolvable(Position start) {
        Set<ColorKey> held = new HashSet<>();
        List<Key> remaining = new ArrayList<>(keys);

        boolean progress = true;
        while (progress) {
            progress = false;
            Set<Position> reach = reachable(start, held);
            for (Key key : new ArrayList<>(remaining)) {
                if (reach.contains(key.getPosition())) {
                    held.add(key.getColorKey());
                    remaining.remove(key);
                    progress = true;
                }
            }
        }
        if (!remaining.isEmpty()) {
            return false;
        }

        Set<Position> finalReach = reachable(start, held);
        for (Room room : lockedRooms) {
            if (!finalReach.contains(room.getCentreRoom())) {
                return false;
            }
        }
        return finalReach.contains(rooms.getLast().getCentreRoom());
    }

    /**
     * метод считает клетки, достижимые от старта с учётом уже собранных ключей:
     * дверь проходима, только если её цвет есть в held
     * @param start стартовая позиция
     * @param held собранные цвета ключей
     * @return множество достижимых клеток
     */
    private Set<Position> reachable(Position start, Set<ColorKey> held) {
        Set<Position> seen = new HashSet<>();
        Deque<Position> queue = new ArrayDeque<>();
        queue.add(start);
        seen.add(start);

        while (!queue.isEmpty()) {
            Position cur = queue.poll();
            for (DirectionType dir : DirectionType.values()) {
                Position next = cur.posDir(dir);
                if (!isInBounds(next.getX(), next.getY()) || seen.contains(next)) {
                    continue;
                }
                TileType tile = map[next.getX()][next.getY()];
                if (tile == TileType.WALL) {
                    continue;
                }
                ColorKey doorColor = colorOfDoorTile(tile);
                if (doorColor != null && !held.contains(doorColor)) {
                    continue;
                }
                seen.add(next);
                queue.add(next);
            }
        }
        return seen;
    }

    /**
     * метод ищет клетки, которыми коридоры прорезали стену комнаты
     * @param room комната
     * @return список входных клеток на периметре комнаты
     */
    private List<Position> findEntrances(Room room) {
        List<Position> entrances = new ArrayList<>();
        for (Corridor corridor : corridors) {
            for (Position p : corridor.getPath()) {
                if (isOnRoomPerimeter(p, room) && isInBounds(p.getX(), p.getY())
                        && map[p.getX()][p.getY()] == TileType.FLOOR
                        && entrances.stream().noneMatch(p::equals)) {
                    entrances.add(p);
                }
            }
        }
        return entrances;
    }

    /**
     * метод проверяет, лежит ли позиция на рамке стен комнаты
     * (эту рамку рисует carveRoom)
     */
    private boolean isOnRoomPerimeter(Position pos, Room room) {
        Position roomPos = room.getPosition();
        int left = roomPos.getX() - 1;
        int right = roomPos.getX() + room.getWidth();
        int top = roomPos.getY() - 1;
        int bottom = roomPos.getY() + room.getHeight();

        boolean insideVertical = pos.getY() >= top && pos.getY() <= bottom;
        boolean insideHorizontal = pos.getX() >= left && pos.getX() <= right;

        boolean onVerticalWall = (pos.getX() == left || pos.getX() == right) && insideVertical;
        boolean onHorizontalWall = (pos.getY() == top || pos.getY() == bottom) && insideHorizontal;

        return onVerticalWall || onHorizontalWall;
    }

    /**
     * метод ищет свободную клетку внутри комнаты под ключ,
     * центр комнаты пропускается - там может стоять игрок или выход на уровень
     * @param room комната
     * @return позиция или null, если свободных клеток нет
     */
    private Position freePositionInside(Room room, Set<Position> zone) {
        Position centre = room.getCentreRoom();
        Position pos = room.getPosition();
        for (int x = pos.getX(); x < pos.getX() + room.getWidth(); x++) {
            for (int y = pos.getY(); y < pos.getY() + room.getHeight(); y++) {
                if (!isInBounds(x, y) || map[x][y] != TileType.FLOOR) {
                    continue;
                }
                // центр занимает игрок или выход на следующий уровень
                if (x == centre.getX() && y == centre.getY()) {
                    continue;
                }
                Position candidate = new Position(x, y);
                if (!zone.contains(candidate)) {
                    continue;
                }
                // предметы и враги расставляются позже и затёрли бы тайл ключа
                if (isOccupiedByContent(room, candidate)) {
                    continue;
                }
                return candidate;
            }
        }
        return null;
    }

    /**
     * метод проверяет, не запланирован ли на клетке предмет или враг комнаты
     */
    private boolean isOccupiedByContent(Room room, Position pos) {
        boolean itemHere = room.getItemList().stream()
                .anyMatch(item -> item.getPosition() != null && pos.equals(item.getPosition()));
        boolean enemyHere = room.getEnemyList().stream()
                .anyMatch(enemy -> enemy.getPosition() != null && pos.equals(enemy.getPosition()));
        return itemHere || enemyHere;
    }

    /**
     * метод возвращает тайл двери нужного цвета
     */
    public static TileType doorTileFor(ColorKey color) {
        return switch (color) {
            case GREEN -> TileType.DOOR_GREEN;
            case BLUE -> TileType.DOOR_BLUE;
            case RED -> TileType.DOOR_RED;
            case YELLOW -> TileType.DOOR_YELLOW;
        };
    }

    /**
     * метод возвращает тайл ключа нужного цвета
     */
    public static TileType keyTileFor(ColorKey color) {
        return switch (color) {
            case GREEN -> TileType.KEY_GREEN;
            case BLUE -> TileType.KEY_BLUE;
            case RED -> TileType.KEY_RED;
            case YELLOW -> TileType.KEY_YELLOW;
        };
    }

    /**
     * метод определяет цвет двери по тайлу
     * @return цвет или null, если тайл не дверь
     */
    public static ColorKey colorOfDoorTile(TileType tile) {
        return switch (tile) {
            case DOOR_GREEN -> ColorKey.GREEN;
            case DOOR_BLUE -> ColorKey.BLUE;
            case DOOR_RED -> ColorKey.RED;
            case DOOR_YELLOW -> ColorKey.YELLOW;
            default -> null;
        };
    }

    /**
     * метод определяет цвет ключа по тайлу
     * @return цвет или null, если тайл не ключ
     */
    public static ColorKey colorOfKeyTile(TileType tile) {
        return switch (tile) {
            case KEY_GREEN -> ColorKey.GREEN;
            case KEY_BLUE -> ColorKey.BLUE;
            case KEY_RED -> ColorKey.RED;
            case KEY_YELLOW -> ColorKey.YELLOW;
            default -> null;
        };
    }

    /**
     * метод проверяет, является ли тайл закрытой дверью
     */
    public static boolean isDoorTile(TileType tile) {
        return colorOfDoorTile(tile) != null;
    }

    /**
     * метод проверяет, лежит ли на клетке что-то подбираемое: предмет или ключ.
     * Враги на такие клетки не встают, иначе затирают их своим символом
     */
    public static boolean isPickupTile(TileType tile) {
        return tile == TileType.ELIXIR || tile == TileType.SCROLL
                || tile == TileType.WEAPON || tile == TileType.FOOD
                || colorOfKeyTile(tile) != null;
    }

    /**
     * метод генерирует комнаты на карте
     */
    private void generateRooms() {
        int roomCount = 10;
        int attempts = 0;
        int maxAttempts = 200;

        while (rooms.size() < roomCount && attempts < maxAttempts) {
            attempts++;
            int gridX = randomNumber(1, MAP_SIZE - 1);
            int gridY = randomNumber(1, MAP_SIZE - 1);
            Room room = new Room(gridX, gridY, difficulty);
            if (!roomIntersectsAny(room)) {
                rooms.add(room);
                carveRoom(room);
            }
        }
    }

    /**
     * метод проверяет, пересекается ли комната с другими комнатами
     * @param room комната
     */
    private boolean roomIntersectsAny(Room room) {
        Position pos = room.getPosition();

        for (Room other : rooms) {
            Position otherPos = other.getPosition();
            boolean intersects = pos.getX() < otherPos.getX() + other.getWidth() + ROOM_PADDING &&
                    pos.getX() + room.getWidth() + ROOM_PADDING > otherPos.getX() &&
                    pos.getY() < otherPos.getY() + other.getHeight() + ROOM_PADDING &&
                    pos.getY() + room.getHeight() + ROOM_PADDING > otherPos.getY();
            if (intersects) {
                return true;
            }
        }
        return false;
    }

    /**
     * метод вырезает комнату на карте
     * @param room комната
     */
    private void carveRoom(Room room) {
        Position pos = room.getPosition();
        for (int x = pos.getX() - 1; x < pos.getX() + room.getWidth() + 1; x++) {
            for (int y = pos.getY() - 1; y < pos.getY() + room.getHeight() + 1; y++) {
                if (isInBounds(x, y)) {
                    if (x == pos.getX() - 1 || x == pos.getX() + room.getWidth() ||
                            y == pos.getY() - 1 || y == pos.getY() + room.getHeight()) {
                        map[x][y] = TileType.WALL;
                    } else {
                        map[x][y] = TileType.FLOOR;
                    }
                }
            }
        }
        for (int x = pos.getX(); x < pos.getX() + room.getWidth(); x++) {
            for (int y = pos.getY(); y < pos.getY() + room.getHeight(); y++) {
                if (isInBounds(x, y)) {
                    map[x][y] = TileType.FLOOR;
                }
            }
        }
    }

    /**
     * метод строит коридоры между комнатами
     */
    private void buildCorridors() {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room currentRoom = rooms.get(i);
            Room nextRoom = rooms.get(i + 1);
            createCorridor(currentRoom, nextRoom);
        }
    }

    /**
     * метод создает коридор между двумя комнатами
     */
    private void createCorridor(Room room1, Room room2) {
        Corridor corridor = new Corridor(room1, room2);
        boolean intersectsOtherRoom = false;
        for (Room room : rooms) {
            if (room != room1 && room != room2) {
                if (corridor.intersectsRoom(room)) {
                    intersectsOtherRoom = true;
                    break;
                }
            }
        }
        if (intersectsOtherRoom) {
            corridor = new Corridor(room1, room2);
        }
        for (Position p : corridor.getPath()) {
            if (isInBounds(p.getX(), p.getY())) {
                if (map[p.getX()][p.getY()] == TileType.WALL) {
                    map[p.getX()][p.getY()] = TileType.FLOOR;
                }
            }
        }
        corridors.add(corridor);
    }

    /**
     * метод перестраивает карту тайлов по уже готовым комнатам и коридорам
     * (используется при загрузке сохранённой игры, без случайной генерации)
     * @param rooms восстановленные комнаты
     * @param corridors восстановленные коридоры
     */
    public void rebuildMap(List<Room> rooms, List<Corridor> corridors) {
        initializeMap();
        this.rooms = new ArrayList<>(rooms);
        this.corridors = new ArrayList<>(corridors);
        for (Room room : rooms) {
            carveRoom(room);
        }
        for (Corridor corridor : corridors) {
            for (Position p : corridor.getPath()) {
                if (isInBounds(p.getX(), p.getY()) && map[p.getX()][p.getY()] == TileType.WALL) {
                    map[p.getX()][p.getY()] = TileType.FLOOR;
                }
            }
        }
    }

    /**
     * метод возвращает соседей комнаты (комнаты, соединенные коридором)
     */
    public List<Room> getConnectedRooms(Room room) {
        List<Room> connected = new ArrayList<>();
        for (Corridor corridor : corridors) {
            if (corridor.getRoom1() == room) {
                connected.add(corridor.getRoom2());
            } else if (corridor.getRoom2() == room) {
                connected.add(corridor.getRoom1());
            }
        }
        return connected;
    }

    private int randomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < mapWidth && y >= 0 && y < mapHeight;
    }



    /**
     * метод проверяет, является ли позиция проходимой
     * @param pos позиция для проверки
     * @return true если можно ходить, false если стена
     */
    public boolean isPositionWalkable(Position pos) {
        if (!isInBounds(pos.getX(), pos.getY())) {
            return false;
        }
        TileType tile = map[pos.getX()][pos.getY()];
        // закрытая дверь непроходима, поэтому враги через неё тоже не ходят
        return tile != TileType.WALL && !isDoorTile(tile);
    }

    /**
     * метод создает игрока на карте
     * @param player игрок
     */
    public void createPlayer(Player player) {
        Position posPlayer = player.getPosition();
        if (player.getIsStunned()) {
            map[posPlayer.getX()][posPlayer.getY()] = TileType.PLAYER_STUNNED;
        } else {
            map[posPlayer.getX()][posPlayer.getY()] = TileType.PLAYER;
        }
    }

    /**
     * метод удаляет игрока с карты
     * @param player игрок
     */
    public void deletePosPlayer(Player player) {
        Position posPlayer = player.getPosition();
        map[posPlayer.getX()][posPlayer.getY()] = TileType.FLOOR;
    }

    /**
     * метод создает игрока в стане на карте
     * @param player игрок
     */
    public void createPlayerStunned(Player player) {
        Position posPlayer = player.getPosition();
        map[posPlayer.getX()][posPlayer.getY()] = TileType.PLAYER_STUNNED;
    }

    /**
     * метод создает предметы на карте
     * @param room комната
     */
    public void createItem(Room room) {
        ArrayList <Item> itemList = room.getItemList();
        for (Item item : itemList) {
            switch (item.getType()) {
                case ELIXIR: map[item.getPosition().getX()][item.getPosition().getY()] = TileType.ELIXIR; break;
                case SCROLL: map[item.getPosition().getX()][item.getPosition().getY()] = TileType.SCROLL; break;
                case FOOD: map[item.getPosition().getX()][item.getPosition().getY()] = TileType.FOOD; break;
                default: map[item.getPosition().getX()][item.getPosition().getY()] = TileType.WEAPON; // [E]
            }
        }
    }

    /**
     * метод создает врагов на карте
     * @param room комната
     */
    public void createEnemies(Room room) {
        ArrayList <Enemies> enemyList = room.getEnemyList();
        for (Enemies enemy : enemyList) {
            if (enemy.getType() == EnemiesType.MIMIC) {
                // Для мимика используем динамический TileType
                map[enemy.getPosition().getX()][enemy.getPosition().getY()] = ((Mimic) enemy).getTileType();
            } else {
                switch (enemy.getType()) {
                    case ZOMBIE: map[enemy.getPosition().getX()][enemy.getPosition().getY()] = TileType.ZOMBIE; break;
                    case OGRE: map[enemy.getPosition().getX()][enemy.getPosition().getY()] = TileType.OGRE; break;
                    case VAMPIRE: map[enemy.getPosition().getX()][enemy.getPosition().getY()] = TileType.VAMPIRE; break;
                    case SNAKE: map[enemy.getPosition().getX()][enemy.getPosition().getY()] = TileType.SNAKE; break;
                    case GHOST: map[enemy.getPosition().getX()][enemy.getPosition().getY()] = TileType.GHOST; break;
                    default: map[enemy.getPosition().getX()][enemy.getPosition().getY()] = TileType.ZOMBIE;
                }
            }
        }
    }

    /**
     * метод создает переход на следующий уровень на карте
     * @param room комната
     */
    public Position createLevel(Room room) {
        Position posLevel = room.getCentreRoom();
        map[posLevel.getX()][posLevel.getY()] = TileType.LEVEL;
        return posLevel;
    }

    /**
     * Выводит карту в консоль для отладки
     */
    public void printMap() {
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                System.out.print(map[x][y].getSymbol());
            }
            System.out.println();
        }
    }


    /**
     * метод удаляет врага с карты
     * @param enemy враг
     */
    public void deleteEnemy(Enemies enemy) {
        Position pos = enemy.getPosition();
        map[pos.getX()][pos.getY()] = TileType.FLOOR;
    }

    /**
     * метод создает врага на карте
     * @param enemy враг
     */
    public void createEnemy(Enemies enemy) {
        Position pos = enemy.getPosition();
        if (enemy.getType() == EnemiesType.MIMIC) {
            // Для мимика используем динамический TileType
            map[pos.getX()][pos.getY()] = ((Mimic) enemy).getTileType();
        } else {
            switch (enemy.getType()) {
                case OGRE: map[pos.getX()][pos.getY()] = TileType.OGRE; break;
                case GHOST: map[pos.getX()][pos.getY()] = TileType.GHOST; break;
                case SNAKE: map[pos.getX()][pos.getY()] = TileType.SNAKE; break;
                case VAMPIRE: map[pos.getX()][pos.getY()] = TileType.VAMPIRE; break;
                case ZOMBIE: map[pos.getX()][pos.getY()] = TileType.ZOMBIE; break;
                default: map[pos.getX()][pos.getY()] = TileType.ZOMBIE;
            }
        }
    }
}
