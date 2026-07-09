package domain.map;

import domain.navigator.Position;

import java.util.ArrayList;
import java.util.List;

public class DungeonGenerator {
    private static final int MAP_SIZE = 15; // 15x15 клеток для комнат
    private static final int ROOM_PADDING = 2; // Отступ между комнатами

    private TileType[][] map;
    private List<Room> rooms;
    private List<Corridor> corridors;
    private int mapWidth;
    private int mapHeight;

    public DungeonGenerator(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.map = new TileType[mapWidth][mapHeight];
        this.rooms = new ArrayList<>();
        this.corridors = new ArrayList<>();
        initializeMap();
    }

    /**
     * Инициализирует карту стенами
     */
    private void initializeMap() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                map[x][y] = TileType.WALL;
            }
        }
    }

    /**
     * Генерирует полное подземелье
     */
    public void generateDungeon() {
        // 1. Генерируем комнаты
        generateRooms();

        // 2. Строим коридоры между комнатами
        buildCorridors();

        // 3. Добавляем дополнительные соединения
        addExtraConnections();

        // 4. Размещаем контент в комнатах
        placeContent();
    }

    /**
     * Генерирует комнаты на карте
     */
    private void generateRooms() {
        int roomCount = 9;
        int attempts = 0;
        int maxAttempts = 100;

        while (rooms.size() < roomCount && attempts < maxAttempts) {
            attempts++;

            // Выбираем позицию в сетке
            int gridX = randomNumber(1, MAP_SIZE - 1);
            int gridY = randomNumber(1, MAP_SIZE - 1);

            // Создаем комнату
            Room room = new Room(gridX, gridY);

            // Проверяем, что комната не пересекается с другими
            if (!roomIntersectsAny(room)) {
                rooms.add(room);
                carveRoom(room);
            }
        }

        System.out.println("Сгенерировано комнат: " + rooms.size());
    }

    /**
     * Проверяет, пересекается ли комната с другими
     */
    private boolean roomIntersectsAny(Room room) {
        Position pos = room.getPosition();

        for (Room other : rooms) {
            Position otherPos = other.getPosition();

            // Проверяем с отступами
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
     * Вырезает комнату на карте
     */
    private void carveRoom(Room room) {
        Position pos = room.getPosition();

        // Стены комнаты
        for (int x = pos.getX() - 1; x < pos.getX() + room.getWidth() + 1; x++) {
            for (int y = pos.getY() - 1; y < pos.getY() + room.getHeight() + 1; y++) {
                if (isInBounds(x, y)) {
                    // Края - стены, внутренность - пол
                    if (x == pos.getX() - 1 || x == pos.getX() + room.getWidth() || y == pos.getY() - 1 || y == pos.getY() + room.getHeight()) {
                        map[x][y] = TileType.WALL;
                    } else {
                        map[x][y] = TileType.FLOOR;
                    }
                }
            }
        }

        // Внутренность комнаты - пол
        for (int x = pos.getX(); x < pos.getX() + room.getWidth(); x++) {
            for (int y = pos.getY(); y < pos.getY() + room.getHeight(); y++) {
                if (isInBounds(x, y)) {
                    map[x][y] = TileType.FLOOR;
                }
            }
        }
    }

    /**
     * Строит коридоры между комнатами
     */
    private void buildCorridors() {
        // Соединяем комнаты последовательно
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room currentRoom = rooms.get(i);
            Room nextRoom = rooms.get(i + 1);

            createCorridor(currentRoom, nextRoom);
        }
    }

    /**
     * Создает коридор между двумя комнатами
     */
    private void createCorridor(Room room1, Room room2) {
        Corridor corridor = new Corridor(room1, room2);

        // Проверяем, что коридор не проходит через другие комнаты
        boolean intersectsOtherRoom = false;
        for (Room room : rooms) {
            if (room != room1 && room != room2) {
                if (corridor.intersectsRoom(room)) {
                    intersectsOtherRoom = true;
                    break;
                }
            }
        }

        // Если коридор пересекает другие комнаты, пробуем меньшую ширину
        if (intersectsOtherRoom) {
            corridor = new Corridor(room1, room2);
        }

        // Вырезаем коридор на карте
        for (Position p : corridor.getPath()) {
            if (isInBounds(p.getX(), p.getY())) {
                // Не заменяем стены комнат на пол
                if (map[p.getX()][p.getY()] == TileType.WALL) {
                    map[p.getX()][p.getY()] = TileType.FLOOR;
                }
            }
        }

        corridors.add(corridor);
    }

    /**
     * Добавляет дополнительные соединения для улучшения связности
     */
    private void addExtraConnections() {
        int extraConnections = rooms.size() / 3;
        int attempts = 0;
        int maxAttempts = 50;

        while (extraConnections > 0 && attempts < maxAttempts) {
            attempts++;

            int idx1 = randomNumber(0, rooms.size() - 1);
            int idx2;
            do {
                idx2 = randomNumber(0, rooms.size() - 1);
            } while (idx1 == idx2);

            // Проверяем, нет ли уже коридора между этими комнатами
            boolean alreadyConnected = false;
            for (Corridor c : corridors) {
                if ((c.getRoom1() == rooms.get(idx1) && c.getRoom2() == rooms.get(idx2))
                        || (c.getRoom1() == rooms.get(idx2) && c.getRoom2() == rooms.get(idx1))) {
                    alreadyConnected = true;
                    break;
                }
            }

            if (!alreadyConnected) {
                createCorridor(rooms.get(idx1), rooms.get(idx2));
                extraConnections--;
            }
        }
    }

    /**
     * Размещает контент в комнатах
     * (враги и предметы уже генерируются в конструкторе Room)
     */
    private void placeContent() {
        // Контент уже сгенерирован в Room
        // Можно добавить дополнительную логику, например, размещение босса в последней комнате
        if (!rooms.isEmpty()) {
            Room lastRoom = rooms.get(rooms.size() - 1);
            // Можно добавить босса или особые предметы
//            System.out.println("Последняя комната: " + lastRoom);
        }
    }

    /**
     * Возвращает соседей комнаты (комнаты, соединенные коридором)
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

    // Вспомогательные методы
    private int randomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < mapWidth && y >= 0 && y < mapHeight;
    }

    // Getters
    public TileType[][] getMap() {
        return map;
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

// Enum для типов клеток
enum TileType {
    WALL('#'), FLOOR('.');

    private char symbol;

    TileType(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}

