package domain.map;

import domain.backpack.Item;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.Position;

import java.util.ArrayList;
import java.util.List;

public class DungeonGenerator {
    private static final int MAP_SIZE = 45;
    private static final int ROOM_PADDING = 5; // Отступ между комнатами

    private TileType[][] map;
    private final List<Room> rooms;
    private final List<Corridor> corridors;
    private final int mapWidth;
    private final int mapHeight;

    public DungeonGenerator(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.map = new TileType[mapWidth][mapHeight];
        this.rooms = new ArrayList<>();
        this.corridors = new ArrayList<>();
        initializeMap();
    }

    /**
     * метод {@link #initializeMap()} инициализирует карту стенами
     */
    private void initializeMap() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                map[x][y] = TileType.WALL;
            }
        }
    }

    /**
     * метод {@link #generateDungeon()} генерирует полное подземелье
     */
    public void generateDungeon() {
        generateRooms();
        buildCorridors();
    }

    /**
     * метод {@link #generateRooms()} генерирует комнаты на карте
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
     * метод {@link #roomIntersectsAny(Room)} проверяет, пересекается ли комната с другими комнатами
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
     * метод {@link #carveRoom(Room)} вырезает комнату на карте
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
     * метод {@link #buildCorridors()} строит коридоры между комнатами
     */
    private void buildCorridors() {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room currentRoom = rooms.get(i);
            Room nextRoom = rooms.get(i + 1);
            createCorridor(currentRoom, nextRoom);
        }
    }

    /**
     * метод {@link #createCorridor(Room, Room)} создает коридор между двумя комнатами
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
     * метод {@link #getConnectedRooms(Room)} возвращает соседей комнаты (комнаты, соединенные коридором)
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

    public TileType[][] getMap() {
        return map;
    }

    public void setMap(TileType[][] map) {
        this.map = map;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Corridor> getCorridors() {
        return corridors;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    /**
     * метод {@link #createPlayer(Player)} создает игрока на карте
     * @param player игрок
     */
    public void createPlayer(Player player) {
        Position posPlayer = player.getPosition();
        map[posPlayer.getX()][posPlayer.getY()] = TileType.PLAYER;
    }

    /**
     * метод {@link #createItem(Room)} создает предметы на карте
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
     * метод {@link #createEnemies(Room)} создает врагов на карте
     * @param room комната
     */
    public void createEnemies(Room room) {
        ArrayList <Enemies> enemyList = room.getEnemyList();
        for (Enemies enemy : enemyList) {
            switch (enemy.getType()) {
                case ZOMBIE: map[enemy.getPosition().getX()][enemy.getPosition().getY()] = TileType.ZOMBIE; break;
                case OGRE: map[enemy.getPosition().getX()][enemy.getPosition().getY()] = TileType.OGRE; break;
                case VAMPIRE: map[enemy.getPosition().getX()][enemy.getPosition().getY()] = TileType.VAMPIRE; break;
                case SNAKE: map[enemy.getPosition().getX()][enemy.getPosition().getY()] = TileType.SNAKE; break;
                default: map[enemy.getPosition().getX()][enemy.getPosition().getY()] = TileType.GHOST;
            }
        }
    }

    /**
     * метод {@link #createLevel(Room)} создает уровень на карте
     * @param room комната
     */
    public void createLevel(Room room) {
        Position posLevel = room.getCentreRoom();
        map[posLevel.getX()][posLevel.getY()] = TileType.LEVEL;
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
}








// clean latter
enum TileType {
    WALL('#'), FLOOR('.'), LEVEL('*'), PLAYER('@'),
    ELIXIR('E'), SCROLL('S'), WEAPON('W'), FOOD('F'),
    ZOMBIE('z'), OGRE('o'), VAMPIRE('v'), GHOST('g'), SNAKE('s');

    private char symbol;

    TileType(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}