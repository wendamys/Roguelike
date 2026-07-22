package domain.map;

import domain.backpack.Item;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.EnemiesType;
import domain.characters.enemies.Mimic;
import domain.navigator.Position;

import java.util.ArrayList;
import java.util.List;

public class DungeonGenerator {
    private static final int MAP_SIZE = 45;
    private static final int ROOM_PADDING = 5; // Отступ между комнатами

    private final int mapWidth = 70;
    private final int mapHeight = 60;
    private TileType[][] map;
    private List<Room> rooms;
    private List<Corridor> corridors;

    public DungeonGenerator() {
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
            Room room = new Room(gridX, gridY);
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

    private boolean isInBounds(int x, int y) {
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
        return map[pos.getX()][pos.getY()] != TileType.WALL;
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
