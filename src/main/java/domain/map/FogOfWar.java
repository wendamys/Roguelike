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

    public boolean[][] getExplored() {
        return explored;
    }

    public void setExplored(boolean[][] explored) {
        this.explored = explored;
    }

    /**
     * метод пересчитывает видимость по позиции игрока и уровню сложности
     * @param player игрок
     * @param rooms комнаты уровня
     * @param difficulty уровень сложности
     */
    public void update(Player player, List<Room> rooms, DifficultyType difficulty) {
        switch (difficulty) {
            case EASY -> revealAll();
            case HARD -> updateHard(player, rooms);
            case VERY_HARD -> updateVeryHard(player);
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
    private void updateVeryHard(Player player) {
        clear(visible);
        clear(explored);
        revealRadius(player.getPosition(), VERY_HARD_RADIUS);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                explored[x][y] = visible[x][y];
            }
        }
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
