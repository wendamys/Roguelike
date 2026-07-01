package domain.map;

import java.util.ArrayList;
import java.util.List;

import static domain.MathUtils.MathUtils.randomNumber;

public class Door {
    private final Room room;
    private boolean isClose;

    public Door(Room room) {
        this.room = room;
        this.isClose = false;
    }

    public int getWidth() {
        return room.getWidth();
    }

    public int getHeight() {
        return room.getHeight();
    }

    public void test() {
        System.out.println("Ширина: " + room.getWidth() + " Высота: " + room.getHeight());
    }

    public boolean getIsClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    /**
     * метод {@link #randomPositionDoorWidth()} рандомно выбирает
     * нахождение двери в зависимости от ширины комнаты
     *
     * @return позицию с дверью по ширине
     */
    public int randomPositionDoorWidth() {
        return randomNumber(1, room.getWidth() - 1);
    }

    /**
     * метод {@link #randomPositionDoorWidth()} рандомно выбирает
     * нахождение двери в зависимости от высоты комнаты
     *
     * @return позицию с дверью по высоте
     */
    public int randomPositionDoorHeight() {
        return randomNumber(1, room.getHeight() - 1);
    }


    /**
     * метод {@link #randomRoomsIsMap()} рандомно создает комнаты на карте
     * @return лист комнат
     */
    public List<Room> randomRoomsIsMap() {
        List<Room> roomList = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            Room room = new Room();
            roomList.add(room);
        }
        for (var i : roomList) {
            System.out.println(i);
        }
        return roomList;
    }


    /**
     * метод {@link #randomCountDoorIsRoom()} рандомно выбирает кол-во дверей
     * в зависимости от нахождения на карте
     * @return лист с количеством дверей
     */
    public List<Integer> randomCountDoorIsRoom() {
        List<Integer> exitDoorList = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            switch (i % 3) {
                case 0 -> { exitDoorList.add(randomNumber(1, 2)); }
                case 1 -> { exitDoorList.add(randomNumber(2, 4)); }
                default -> { exitDoorList.add(randomNumber(1, 3)); }
            }
        }
        return exitDoorList;
    }

    /**
     * метод {@link #randomCountDoorIsRoom()} рандомно выбирает кол-во дверей
     * в зависимости от нахождения на карте
     * @return лист с количеством дверей
     */
    public void generationDoorIsRoom() {
        List<Integer> listInteger = randomCountDoorIsRoom();
        List<Integer> listIntegerDoor = new ArrayList<>();

        for (Integer i: listInteger) {
//            for (int j = 1; i > j; j++) {
//                System.out.println(i);
//                listIntegerDoor.add(randomDoorIsWall());
            }
//            listIntegerDoor.add(0);
        }
//        return listIntegerDoor;


    public int randomDoorIsWall() {
        return randomNumber(1, 4);
            // 1 System.out.println("Дверь слева");
            // 2 System.out.println("Дверь справа");
            // 3 System.out.println("Дверь снизу");
            // 4 System.out.println("Дверь сверху");
    }


    public int exitCountDoor() {
        List<Room> room = randomRoomsIsMap();
        return 0;
    }


}
