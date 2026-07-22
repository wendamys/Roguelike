package domain.map;

import domain.characters.Player;
import domain.gameSession.DifficultyType;
import domain.navigator.Position;

import java.util.List;

/**
 * Туман войны хранит, что игрок видит прямо сейчас и что он уже разведал.
 * Живёт в domain, а не в presentation, потому что разведанное попадает в сохранение.
 */
public class FogOfWar {

    // радиус обзора на VERY HARD
    private static final int VERY_HARD_RADIUS = 3;
    // радиус обзора в коридоре на HARD
    private static final int HARD_CORRIDOR_RADIUS = 1;

    private final int width;
    private final int height;
    private boolean[][] explored;
    private boolean[][] visible;

    public FogOfWar(int width, int height) {
        this.width = width;
        this.height = height;
        this.explored = new boolean[width][height];
        this.visible = new boolean[width][height];
    }

    public boolean isVisible(int x, int y) {
        return isInBounds(x, y) && visible[x][y];
    }

    public boolean isExplored(int x, int y) {
        return isInBounds(x, y) && explored[x][y];
    }

    /**
     * метод отдаёт копию разведанного, чтобы состояние тумана нельзя было менять снаружи
     */
    public boolean[][] getExplored() {
        boolean[][] copy = new boolean[width][height];
        for (int x = 0; x < width; x++) {
            System.arraycopy(explored[x], 0, copy[x], 0, height);
        }
        return copy;
    }

    /**
     * метод восстанавливает разведанное из сохранения.
     * Копирует поклеточно и игнорирует лишнее: сейв мог быть снят на карте другого размера,
     * и хранение чужого массива по ссылке уронило бы отрисовку по выходу за границы
     * @param source разведанные клетки из сохранения
     */
    public void setExplored(boolean[][] source) {
        if (source == null) {
            return;
        }
        boolean[][] restored = new boolean[width][height];
        int maxX = Math.min(width, source.length);
        for (int x = 0; x < maxX; x++) {
            if (source[x] == null) {
                continue;
            }
            int maxY = Math.min(height, source[x].length);
            System.arraycopy(source[x], 0, restored[x], 0, maxY);
        }
        this.explored = restored;
    }

    /**
     * метод пересчитывает видимость по позиции игрока и уровню сложности
     * @param player игрок
     * @param rooms комнаты уровня
     * @param difficulty уровень сложности
     */
    public void update(Player player, List<Room> rooms, DifficultyType difficulty, TileType[][] map) {
        switch (difficulty) {
            case EASY -> revealAll();
            case HARD -> updateHard(player, rooms);
            case VERY_HARD -> updateVeryHard(player, map);
        }
    }

    /**
     * метод открывает всю карту целиком (сложность EASY)
     */
    private void revealAll() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                visible[x][y] = true;
                explored[x][y] = true;
            }
        }
    }

    /**
     * метод открывает комнату целиком при входе в неё, разведанное накапливается
     * @param player игрок
     * @param rooms комнаты уровня
     */
    private void updateHard(Player player, List<Room> rooms) {
        clear(visible);

        Room current = findRoomAt(player.getPosition(), rooms);
        if (current != null) {
            revealRoom(current);
        } else {
            revealRadius(player.getPosition(), HARD_CORRIDOR_RADIUS);
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (visible[x][y]) {
                    explored[x][y] = true;
                }
            }
        }
    }

    /**
     * метод открывает только радиус вокруг игрока, память карты не ведётся
     * @param player игрок
     */
    private void updateVeryHard(Player player, TileType[][] map) {
        clear(visible);
        clear(explored);
        revealRadius(player.getPosition(), VERY_HARD_RADIUS);
        hideWallsBehindWalls(map);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                explored[x][y] = visible[x][y];
            }
        }
    }

    /**
     * метод оставляет видимой только первую линию стен - ту, что примыкает к видимому полу.
     * Карта залита стенами везде, где нет комнат и коридоров, и без этого игрок видел бы
     * сплошной массив '#' на всю глубину радиуса
     * @param map тайлы карты
     */
    private void hideWallsBehindWalls(TileType[][] map) {
        boolean[][] filtered = new boolean[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!visible[x][y]) {
                    continue;
                }
                filtered[x][y] = map[x][y] != TileType.WALL || touchesVisibleOpenCell(map, x, y);
            }
        }
        visible = filtered;
    }

    /**
     * метод проверяет, граничит ли стена с видимой проходимой клеткой
     * (включая диагонали, иначе углы комнат выглядят рваными)
     */
    private boolean touchesVisibleOpenCell(TileType[][] map, int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nx = x + dx;
                int ny = y + dy;
                if (isInBounds(nx, ny) && visible[nx][ny] && map[nx][ny] != TileType.WALL) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * метод помечает видимой комнату вместе с её стенами-периметром
     * @param room комната
     */
    private void revealRoom(Room room) {
        Position pos = room.getPosition();
        for (int x = pos.getX() - 1; x <= pos.getX() + room.getWidth(); x++) {
            for (int y = pos.getY() - 1; y <= pos.getY() + room.getHeight(); y++) {
                if (isInBounds(x, y)) {
                    visible[x][y] = true;
                }
            }
        }
    }

    /**
     * метод помечает видимым квадрат заданного радиуса вокруг точки
     * @param centre центр
     * @param radius радиус
     */
    private void revealRadius(Position centre, int radius) {
        for (int x = centre.getX() - radius; x <= centre.getX() + radius; x++) {
            for (int y = centre.getY() - radius; y <= centre.getY() + radius; y++) {
                if (isInBounds(x, y)) {
                    visible[x][y] = true;
                }
            }
        }
    }

    /**
     * метод ищет комнату, внутри которой стоит игрок (с учётом стен-периметра)
     * @param pos позиция игрока
     * @param rooms комнаты уровня
     * @return комната или null, если игрок в коридоре
     */
    private Room findRoomAt(Position pos, List<Room> rooms) {
        for (Room room : rooms) {
            Position roomPos = room.getPosition();
            boolean inside = pos.getX() >= roomPos.getX() - 1
                    && pos.getX() <= roomPos.getX() + room.getWidth()
                    && pos.getY() >= roomPos.getY() - 1
                    && pos.getY() <= roomPos.getY() + room.getHeight();
            if (inside) {
                return room;
            }
        }
        return null;
    }

    private void clear(boolean[][] grid) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = false;
            }
        }
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
