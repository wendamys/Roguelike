package domain.map;

import domain.navigator.Position;

import java.util.ArrayList;
import java.util.List;

public class Corridor {
    private final List<Position> path; // Путь коридора
    private final Room room1;
    private final Room room2;

    public Corridor(Room room1, Room room2) {
        this.room1 = room1;
        this.room2 = room2;
        this.path = new ArrayList<>();
        generateCorridor();
    }

    public List<Position> getPath() { return path; }
    public Room getRoom1() { return room1; }
    public Room getRoom2() { return room2; }

    /**
     * метод {@link #generateCorridor()} генерирует корридор между 2 комнатами
     */
    private void generateCorridor() {
        Position center1 = room1.getPosition().copy();
        Position center2 = room2.getPosition().copy();

        Position realCenter1 = new Position(
                center1.getX() + room1.getWidth() / 2,
                center1.getY() + room1.getHeight() / 2
        );
        Position realCenter2 = new Position(
                center2.getX() + room2.getWidth() / 2,
                center2.getY() + room2.getHeight() / 2
        );

        createHorizontalPath(realCenter1, realCenter2);
        createVerticalPath(realCenter1, realCenter2);
    }

    /**
     * метод {@link #createHorizontalPath(Position, Position)}
     * создает позицию пути корридора по горизонтали
     * @param center1 центр 1 комнаты
     * @param center2 центр 2 комнаты
     */
    private void createHorizontalPath(Position center1, Position center2) {
        int startX = Math.min(center1.getX(), center2.getX());
        int endX = Math.max(center1.getX(), center2.getX());
        int y = center1.getY();

        for (int x = startX; x <= endX; x++) {
            path.add(new Position(x, y));
        }
    }

    /**
     * метод {@link #createVerticalPath(Position, Position)}
     * создает позицию пути корридора по вертикали
     * @param center1 центр 1 комнаты
     * @param center2 центр 2 комнаты
     */
    private void createVerticalPath(Position center1, Position center2) {
        int startY = Math.min(center1.getY(), center2.getY());
        int endY = Math.max(center1.getY(), center2.getY());
        int x = center2.getX();

        for (int y = startY; y <= endY; y++) {
            path.add(new Position(x, y));
        }
    }

    /**
     * метод {@link #intersectsRoom(Room)} проверяет преесечение корридора и комнаты
     * @param room комната
     * @return true - пересекает, false - нет
     */
    public boolean intersectsRoom(Room room) {
        Position roomPos = room.getPosition();
        int roomX1 = roomPos.getX();
        int roomX2 = roomPos.getX() + room.getWidth();
        int roomY1 = roomPos.getY();
        int roomY2 = roomPos.getY() + room.getHeight();

        for (var p : path) {
            if (p.getX() >= roomX1 && p.getX() <= roomX2 &&
                    p.getY() >= roomY1 && p.getY() <= roomY2) {
                return true;
            }
        }
        return false;
    }
}
