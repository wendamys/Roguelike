package domain.map;

import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.backpack.items.Elixir;
import domain.backpack.items.Food;
import domain.backpack.items.Scroll;
import domain.backpack.items.Weapon;
import domain.characters.Enemies;
import domain.characters.enemies.*;
import domain.navigator.Position;

import java.util.ArrayList;
import java.util.List;

import static domain.MathUtils.MathUtils.randomNumber;

public class Room {

    private final int width;
    private final int height;
    private final int area;
    private int capacityEnemy;
    private int capacityItem;

    private Position position;
    private RoomType roomType;

    private final ArrayList<Enemies> enemyList = new ArrayList<>(capacityEnemy);
    private final ArrayList<Item> itemList = new ArrayList<>(capacityItem);

    public Room(int x, int y) {
        this.width = randomNumber(5, 12);
        this.height = randomNumber(5, 12);
        this.area = width * height;

        this.position = randomPositionRoom(x, y);
        this.setRoomType(area);
        this.setCapacityEnemy(capacityEnemy);
        this.setCapacityItem(capacityItem);

        // добавление врагов и предметов в пул комнаты
        addEnemyList();
        addItemList();
    }

    public RoomType getRoomType() { return roomType; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public Position getPosition() { return position; }

    public void setCapacityEnemy(int capacity) {
        this.capacityEnemy = randomCapacityValueEnemy(roomType);
    }

    public void setCapacityItem(int capacity) {
        this.capacityItem = randomCapacityValueItem(roomType);
    }

    /**
     * метод {@link #setRoomType(int)} определяет тип комнаты в зависимости от ее площади
     * @param area площаль комнаты
     */
    public void setRoomType(int area) {
        if (area >= 25 && 55 >= area) this.roomType = RoomType.SMALL;
        else if (area >= 56 && 107 >= area) this.roomType = RoomType.MIDDLE;
        else if (area >= 108 && 144 >= area) this.roomType = RoomType.BIG;
    }

    /**
     * метод {@link #randomCapacityValueEnemy(RoomType)} рандомит размерность пулла врагов
     * в зависимости от типа комнаты
     * @param roomType тип комнаты
     * @return размерность пула врагов
     */
    private int randomCapacityValueEnemy(RoomType roomType) {
        return switch (roomType) {
            case SMALL -> randomNumber(0, 1);
            case MIDDLE -> randomNumber(1, 2);
            case BIG -> randomNumber(1, 3);
        };
    }

    /**
     * метод {@link #randomCapacityValueItem(RoomType)} рандомит размерность пулла предметов
     * в зависимости от типа комнаты
     * @param roomType тип комнаты
     * @return размерность пула предметов
     */
    private int randomCapacityValueItem(RoomType roomType) {
        return switch (roomType) {
            case SMALL -> randomNumber(0, 1);
            case MIDDLE -> randomNumber(1, 3);
            case BIG -> randomNumber(2, 4);
        };
    }

    /**
     * метод {@link #addEnemyList()} заполняет весь лист рандомными врагами
     */
    private void addEnemyList() {
        for(int i = 0; i < capacityEnemy; i++) {
            addEnemyValue(randomEnemy());
        }
        // clean later
        for(var i: enemyList) System.out.println("\n" + i);
    }

    /**
     * метод {@link #addEnemyValue(EnemiesType)} добавляет врага в пул врагов
     * @param enemiesType тип врага
     */
    private void addEnemyValue(EnemiesType enemiesType) {
        switch (enemiesType) {
            case ZOMBIE -> enemyList.add(new Zombie(randomPosition()));
            case OGRE -> enemyList.add(new Ogre(randomPosition()));
            case VAMPIRE -> enemyList.add(new Vampire(randomPosition()));
            case SNAKE -> enemyList.add(new Snake(randomPosition()));
            case MIMIC -> enemyList.add(new Mimic(randomPosition()));
            case GHOST -> enemyList.add(new Ghost(randomPosition()));
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

    /**
     * метод {@link #addItemList()} заполняет весь лист рандомными предметами
     */
    private void addItemList() {
        for(int i = 0; i < capacityItem; i++) {
            addItemValue(randomItem());
        }
        // clean later
        for(var i: itemList) System.out.println("\n" + i);
    }

    /**
     * метод {@link #addItemValue(ItemsType)} добавляет предмет в пул предметов
     * @param itemsType тип предмета
     */
    private void addItemValue(ItemsType itemsType) {
        switch (itemsType) {
            case ELIXIR -> itemList.add(new Elixir(randomPosition()));
            case FOOD -> itemList.add(new Food(randomPosition()));
            case SCROLL -> itemList.add(new Scroll(randomPosition()));
            case WEAPON -> itemList.add(new Weapon(randomPosition()));
        }
    }

    /**
     * метод {@link #randomItem()} рандомно выбирает тип предмета
     * @return тип предмета
     */
    private ItemsType randomItem() {
        switch (randomNumber(1, 6)) {
            case 1 -> { return ItemsType.ELIXIR; }
            case 2 -> { return  ItemsType.FOOD; }
            case 3 -> { return ItemsType.SCROLL; }
            case 4 -> { return  ItemsType.WEAPON; }
            default -> { return  ItemsType.FOOD; }
        }
    }

    /**
     * метод {@link #randomPositionRoom(int, int)} создает рандомную позицию относительно сетки матрицы
     * в которой находится комната
     * @param x координата X
     * @param y координата Y
     * @return позиция комнаты
     */
    private Position randomPositionRoom(int x, int y) {
        int dx = randomNumber(x * 15 + 1, (x * 15) + 15 - width - 1);
        int dy = randomNumber(y * 15 + 1, (y * 15) + 15 - height - 1);
        return new Position(dx, dy);
    }

    /**
     * метод {@link #randomPosition()} создает рандомную позицию для объекта в комнате
     * @return позиция объекта
     */
    private Position randomPosition() {
        return new Position(
                randomNumber(position.getX() + 1, position.getX() + width - 1),
                randomNumber(position.getY() + 1, position.getY() + height - 1)
        );
    }

    /**
     * метод {@link #contains(Position)} проверяет, находится ли позиция внутри комнаты
     * @param pos позиция
     * @return true - внутри, false - снаружи
     */
    public boolean contains(Position pos) {
        return pos.getX() >= position.getX() &&
                pos.getX() < position.getX() + width &&
                pos.getY() >= position.getY() &&
                pos.getY() < position.getY() + height;
    }

    /**
     * метод {@link #isOnBorder(Position)} проверяет, находится ли позиция на границе комнаты
     * @param pos позиция
     * @return true - на гранце, false - нет
     */
    public boolean isOnBorder(Position pos) {
        return (pos.getX() == position.getX() ||
                pos.getX() == position.getX() + width - 1 ||
                pos.getY() == position.getY() ||
                pos.getY() == position.getY() + height - 1);
    }

    /**
     * метод {@link #getBorderPositions()} получает все позиции на границе комнаты
     * @return лист с позициями
     */
    public List<Position> getBorderPositions() {
        List<Position> borders = new ArrayList<>();
        for (int x = position.getX(); x < position.getX() + width; x++) {
            borders.add(new Position(x, position.getY()));
            borders.add(new Position(x, position.getY() + height - 1));
        }
        for (int y = position.getY(); y < position.getY() + height; y++) {
            borders.add(new Position(position.getX(), y));
            borders.add(new Position(position.getX() + width - 1, y));
        }
        return borders;
    }

    @Override
    public String toString() {
        return String.format(
                "\nRoom:\nwidth: %d\nheight: %d\narea: %d\nroomType: %s\ncapacityEnemy: %d\ncapacityItem: %d\nposition(%d, %d)",
                width,
                height,
                area,
                roomType,
                capacityEnemy,
                capacityItem,
                position.getX(),
                position.getY()
        );
    }
}