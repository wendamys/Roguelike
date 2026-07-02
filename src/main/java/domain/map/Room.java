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

    public Room() {
        this.width = randomNumber(4, 12);
        this.height = randomNumber(4, 12);
        this.area = width * height;

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

    public void setCapacityEnemy(int capacity) {
        this.capacityEnemy = randomCapacityValueEnemy(roomType);
    }

    public void setCapacityItem(int capacity) {
        this.capacityItem = randomCapacityValueItem(roomType);
    }

    public void setRoomType(int area) {
        if (area >= 16 && 48 >= area) this.roomType = RoomType.SMALL;
        else if (area >= 49 && 99 >= area) this.roomType = RoomType.MIDDLE;
        else if (area >= 100 && 144 >= area) this.roomType = RoomType.BIG;
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
    public void addEnemyList() {
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
    public void addItemList() {
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
     * метод {@link #randomPosition()} создает рандомную позицию для объекта в комнате
     * @return позиция объекта
     */
    private Position randomPosition() {
        return new Position(
                randomNumber(1, width),
                randomNumber(1, height)
        );
    }

    @Override
    public String toString() {
        return String.format(
                "\nRoom:\nwidth: %d\nheight: %d\narea: %d\nroomType: %s\ncapacityEnemy: %d\ncapacityItem: %d",
                width,
                height,
                area,
                roomType,
                capacityEnemy,
                capacityItem
        );
    }


}