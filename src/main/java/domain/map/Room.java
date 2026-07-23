package domain.map;

import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.backpack.items.Elixir;
import domain.backpack.items.Food;
import domain.backpack.items.Scroll;
import domain.backpack.items.Weapon;
import domain.characters.Enemies;
import domain.characters.enemies.*;
import domain.gameSession.DifficultyType;
import domain.navigator.Position;

import java.util.ArrayList;

import static domain.MathUtils.MathUtils.randomNumber;

public class Room {

    private int width;
    private int height;
    private final int area;
    private int capacityEnemy;
    private int capacityItem;

    private Position position;
    private RoomType roomType;
    private final DifficultyType difficulty;
    private Door door; // null, если комната не заперта

    private ArrayList<Enemies> enemyList = new ArrayList<>(capacityEnemy);
    private ArrayList<Item> itemList = new ArrayList<>(capacityItem);

    public Room(int x, int y) {
        this(x, y, DifficultyType.EASY);
    }

    public Room(int x, int y, DifficultyType difficulty) {
        this.difficulty = difficulty;
        this.width = randomNumber(5, 12);
        this.height = randomNumber(5, 12);
        this.area = width * height;
        this.position = new Position(x, y);
        this.setRoomType(area);
        this.setCapacityEnemy(capacityEnemy);
        this.setCapacityItem(capacityItem);

        // добавление врагов и предметов в пул комнаты
        addEnemyList();
        addItemList();
    }

    public int getWidth() { return width; }
    public void setWidth(int width) {this.width = width;}

    public int getHeight() { return height; }
    public void setHeight(int height) {this.height = height;}

    public int getArea() { return area; }

    public int getCapacityEnemy() { return capacityEnemy; }
    public int getCapacityItem() { return capacityItem; }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public RoomType getRoomType() { return roomType; }

    public Door getDoor() { return door; }
    public void setDoor(Door door) { this.door = door; }

    public ArrayList<Enemies> getEnemyList() {return enemyList;}
    public void setEnemyList(ArrayList<Enemies> enemyList) {this.enemyList = enemyList;}

    public ArrayList<Item> getItemList() {return itemList;}
    public void setItemList(ArrayList<Item> itemList) {this.itemList = itemList;}


    public void setCapacityEnemy(int capacity) {
        this.capacityEnemy = randomCapacityValueEnemy(roomType);
    }

    public void setCapacityItem(int capacity) {
        this.capacityItem = randomCapacityValueItem(roomType);
    }

    public Position getCentreRoom() {
        return new Position(
                position.getX() + width / 2,
                position.getY() + height / 2
        );
    }

    /**
     * метод определяет тип комнаты в зависимости от ее площади
     * @param area площаль комнаты
     */
    public void setRoomType(int area) {
        if (area >= 25 && 55 >= area) this.roomType = RoomType.SMALL;
        else if (area >= 56 && 107 >= area) this.roomType = RoomType.MIDDLE;
        else if (area >= 108 && 144 >= area) this.roomType = RoomType.BIG;
    }

    /**
     * метод рандомит размерность пулла врагов
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
     * метод рандомит размерность пулла предметов
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
     * метод заполняет весь лист рандомными врагами
     */
    private void addEnemyList() {
        for(int i = 0; i < capacityEnemy; i++) {
            addEnemyValue(randomEnemy());
        }
    }

    /**
     * метод добавляет врага в пул врагов
     * @param enemiesType тип врага
     */
    private void addEnemyValue(EnemiesType enemiesType) {
        Enemies enemy = switch (enemiesType) {
            case ZOMBIE -> new Zombie(randomPosition(), difficulty);
            case OGRE -> new Ogre(randomPosition(), difficulty);
            case VAMPIRE -> new Vampire(randomPosition(), difficulty);
            case SNAKE -> new Snake(randomPosition(), difficulty);
            case MIMIC -> new Mimic(randomPosition(), difficulty);
            case GHOST -> new Ghost(randomPosition(), difficulty);
        };
        applyDifficulty(enemy);
        enemyList.add(enemy);
    }

    /**
     * метод домножает статы врага на коэффициент сложности
     * maxHealth пересчитывается обязательно: конструкторы врагов выставляют его
     * сразу после setHealthBegin, до применения коэффициента
     * @param enemy враг
     */
    private void applyDifficulty(Enemies enemy) {
        double coef = difficulty.getCoef();
        if (coef == 1.0) {
            return;
        }
        // масштабируем через штатные *Rand-сеттеры, как требует соглашение проекта
        enemy.setHealthBegin((int) (enemy.getHealth() * coef));
        enemy.setMaxHealth(enemy.getHealth());
        enemy.setStrengthRand((int) (enemy.getStrength() * coef));
    }

    /**
     * метод рандомно выбирает тип врага
     * @return тип врага
     */
    private EnemiesType randomEnemy() {
        switch (randomNumber(1, 6)) {
            case 1 -> { return  EnemiesType.OGRE; }
            case 2 -> { return EnemiesType.VAMPIRE; }
            case 3 -> { return  EnemiesType.SNAKE; }
            case 4 -> { return EnemiesType.MIMIC; }
            case 5 -> { return  EnemiesType.GHOST; }
            default -> { return  EnemiesType.ZOMBIE; }
        }
    }

    /**
     * метод заполняет весь лист рандомными предметами
     */
    private void addItemList() {
        for(int i = 0; i < capacityItem; i++) {
            addItemValue(randomItem());
        }
    }

    /**
     * метод добавляет предмет в пул предметов
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
     * метод рандомно выбирает тип предмета
     * @return тип предмета
     */
    private ItemsType randomItem() {
        switch (randomNumber(1, 10)) {
            case 1 -> { return ItemsType.ELIXIR; }
            case 2, 3, 4 -> { return ItemsType.SCROLL; }
            case 10 -> { return  ItemsType.WEAPON; }
            default -> { return  ItemsType.FOOD; }
        }
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
