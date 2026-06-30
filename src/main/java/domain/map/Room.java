package domain.map;

import domain.backpack.Item;
import domain.characters.Enemies;
import domain.characters.enemies.*;
import domain.navigator.Position;

import java.util.ArrayList;

import static domain.MathUtils.MathUtils.randomNumber;

public class Room {
    private final Level level;
    private final int width;
    private final int height;
    private final int area;
    private RoomType roomType;
    private int capacity;

    private Position position;
    private final ArrayList<Enemies> enemyList = new ArrayList<>(capacity);
    private ArrayList<Item> itemList = new ArrayList<>();

    public Room(Level level) {
        this.level = level;
        this.width = randomNumber(4, 12);
        this.height = randomNumber(4, 12);
        this.area = width * height;
        this.setRoomType(area);
        this.setCapacity(capacity);
    }

    public Level getLevel() {
        return level;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getArea() {
        return area;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setCapacity(int capacity) {
        this.capacity = randomCapacityValue(roomType);
    }

    public void setRoomType(int area) {
        if (area >= 16 && 48 >= area) this.roomType = RoomType.SMALL;
        else if (area >= 49 && 99 >= area) this.roomType = RoomType.MIDDLE;
        else if (area >= 100 && 144 >= area) this.roomType = RoomType.BIG;
    }

    /**
     * метод {@link #randomCapacityValue(RoomType)} рандомит размерность пулла врагов в зависимости от типа комнаты
     * @param roomType тип комнаты
     * @return размерность пула врагов
     */
    private int randomCapacityValue(RoomType roomType) {
        return switch (roomType) {
            case SMALL -> randomNumber(0, 1);
            case MIDDLE -> randomNumber(1, 2);
            case BIG -> randomNumber(1, 3);
        };
    }

    /**
     * метод {@link #addEnemyList()} заполняет весь лист врагов
     */
    public void addEnemyList() {
        for(int i = 0; i < capacity; i++) {
            addEnemyValue(randomEnemy());
        }
    }

    /**
     * метод {@link #addEnemyValue(EnemiesType)} добавляет врага в пул врагов
     * @param enemiesType тип врага
     */
    private void addEnemyValue(EnemiesType enemiesType) {
        switch (enemiesType) {
            case ZOMBIE -> enemyList.add(new Zombie(new Position(0, 0)));
            case OGRE -> enemyList.add(new Ogre(new Position(1, 1)));
            case VAMPIRE -> enemyList.add(new Vampire(new Position(2, 2)));
            case SNAKE -> enemyList.add(new Snake(new Position(3, 3)));
            case MIMIC -> enemyList.add(new Mimic(new Position(4, 4)));
            case GHOST -> enemyList.add(new Ghost(new Position(5, 5)));
        }
    }

    /**
     * метод {@link  #randomEnemy()} рандомно выбирает тип врага
     * @return тип врага
     */
    private EnemiesType randomEnemy() {
        switch (randomNumber(1, 6)) {
            case 1 -> { return EnemiesType.ZOMBIE; }
            case 2 -> { return  EnemiesType.OGRE; }
            case 3 -> { return EnemiesType.VAMPIRE; }
            case 4 -> { return  EnemiesType.SNAKE; }
            case 5 -> { return EnemiesType.MIMIC; }
            case 6 -> { return  EnemiesType.GHOST; }
            default -> { return  EnemiesType.ZOMBIE; }
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Room:\nwidth: %d\nheight: %d\narea: %d\nroomType: %s\ncapacity: %d",
                width,
                height,
                area,
                roomType,
                capacity
        );
    }
}