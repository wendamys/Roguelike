package domain.map;


import java.util.ArrayList;
import java.util.List;

import static domain.MathUtils.MathUtils.randomNumber;
import static domain.navigator.DirectionType.*;

import domain.navigator.DirectionType;
import domain.navigator.Position;

public class Door {
    private final Room room;
    private boolean isClose;
    private List <Door> completeAllDoor = new ArrayList<>();

    // цвет двери привязан к комнате: все входы в неё одного цвета
    // и открываются одним ключом, сколько бы проходов ни нарезал генератор
    private ColorKey colorKey;
    private List<Position> entrances = new ArrayList<>();

    public Door(Room room) {
        this.room = room;
        this.isClose = false;
    }

    public Door(Room room, ColorKey colorKey, List<Position> entrances) {
        this.room = room;
        this.colorKey = colorKey;
        this.entrances = new ArrayList<>(entrances);
        this.isClose = true;
    }

    public ColorKey getColorKey() {
        return colorKey;
    }

    public List<Position> getEntrances() {
        return entrances;
    }

    public int getWidth() {
        return room.getWidth();
    }
    public int getHeight() {
        return room.getHeight();
    }
    public boolean getIsClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    /**
     * метод нужен для создания массива с открытыми дверями
     */
    public void randomCreateDoorRoom(List<Room> arrayIsRoom, List<Integer> numberDoor) {
        for (int i = 0; i < arrayIsRoom.size(); i++) { // мы попадаем в комнату
            List<DirectionType> openDirList = openDir(i);
            System.out.println("\nOPEN: " + openDirList);
            int number = numberDoor.get(i);
            List<DirectionType> arrayDay = selectDirIsOpenAndRandomCountDir(openDirList, number);
            System.out.println("Сгенерированные двери для комнаты: " + i + " [" + arrayDay + "]");
        }
    }

    private List<DirectionType> selectDirIsOpenAndRandomCountDir(List<DirectionType> openDirList, Integer size) {
        List<DirectionType> arrayDir = new ArrayList<>();
        for (int j = 0; size > j; j++) {
            DirectionType dirType = DirectionType.random();
            System.out.println(dirType);
            if (!(arrayDir.contains(dirType)) && openDirList.contains(dirType)) {
                arrayDir.add(dirType);
                j++;
            }
            j--;
        }
        return arrayDir;
    }

    public void setCompleteAllDoor() {
        List<Room> roomList = new ArrayList<>();
//        roomList = randomRoomsIsMap();
//        System.out.println(roomList);

    }

    /**
     * метод {@link #openDir(int numRoom)} определяет какие направления двери открыты
     * в зависимости от положения комнаты
     * @return лист направлений дверей
     */
    private List<DirectionType> openDir(int numRoom) {
        List<DirectionType> arrayDirTrue = new ArrayList<>();
        switch (numRoom) {
            case 0 -> {
                arrayDirTrue.add(DOWN);
                arrayDirTrue.add(RIGHT);
            }
            case 1 -> {
                arrayDirTrue.add(DOWN);
                arrayDirTrue.add(LEFT);
                arrayDirTrue.add(RIGHT);
            }
            case 2 -> {
                arrayDirTrue.add(DOWN);
                arrayDirTrue.add(LEFT);
            }
            case 3 -> {
                arrayDirTrue.add(FORWARD);
                arrayDirTrue.add(DOWN);
                arrayDirTrue.add(RIGHT);

            }
            case 4 -> {
                arrayDirTrue.add(FORWARD);
                arrayDirTrue.add(DOWN);
                arrayDirTrue.add(LEFT);
                arrayDirTrue.add(RIGHT);
            }
            case 5 -> {
                arrayDirTrue.add(FORWARD);
                arrayDirTrue.add(DOWN);
                arrayDirTrue.add(LEFT);
            }
            case 6 -> {
                arrayDirTrue.add(FORWARD);
                arrayDirTrue.add(RIGHT);
            }
            case 7 -> {
                arrayDirTrue.add(FORWARD);
                arrayDirTrue.add(LEFT);
                arrayDirTrue.add(RIGHT);
            }
            default -> {
                arrayDirTrue.add(FORWARD);
                arrayDirTrue.add(LEFT);
            }
        }
        return arrayDirTrue;
    }

    /**
     * метод рандомно выбирает
     * нахождение двери в зависимости от ширины комнаты
     *
     * @return позицию с дверью по ширине
     */
    public int randomPositionDoorWidth() {
        return randomNumber(1, room.getWidth() - 1);
    }

    /**
     * метод рандомно выбирает
     * нахождение двери в зависимости от высоты комнаты
     *
     * @return позицию с дверью по высоте
     */
    public int randomPositionDoorHeight() {
        return randomNumber(1, room.getHeight() - 1);
    }

    /**
     * метод рандомно выбирает кол-во дверей
     * в зависимости от нахождения на карте
     * @return лист с количеством дверей
     */
    public List<Integer> randomCountDoorIsRoom() {
        List<Integer> exitDoorList = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            if (i % 2 == 0 && i != 4) { exitDoorList.add(randomNumber(1, 2)); }
            if (i % 2 == 1) {exitDoorList.add(randomNumber(1, 3)); }
            if (i == 4) { exitDoorList.add(randomNumber(2, 4)); }
        }
        return exitDoorList;
    }

    /**
     * метод  рандомно создает комнаты на карте
     * @return лист комнат
     */
//    public List<Room> randomRoomsIsMap() {
//        List<Room> roomList = new ArrayList<>(9);
//        for (int i = 0; i < 9; i++) roomList.add(new Room());
//        return roomList;
//    }
}
