package domain.map;

import domain.MathUtils.MathUtils;
import domain.navigator.DirectionType;

import java.util.ArrayList;
import java.util.List;

import static domain.MathUtils.MathUtils.randomDirection;
import static domain.MathUtils.MathUtils.randomNumber;
import static domain.navigator.DirectionType.*;

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
    public boolean getIsClose() {
        return isClose;
    }


    public void test() {
        System.out.println("Ширина: " + room.getWidth() + " Высота: " + room.getHeight());
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

    // мне нужно передать сюда лист с комнатами, чтобы в каждую комнату
    // в зависимости от ее положения на карте вызвалась функция
    // которая выбирает сколько будет дверей
    // после нужно вызвать функцию от количества дверей в каких направлениях они будут
    // также нужно учесть что двери должны быть в разных направлениях
    // и если они есть уже в одном из, то генерация в этой стороне происходить не должна
    public void randomCreateDoorRoom(List<Room> roomList) {

        randomPositionDoorHeight();
        randomPositionDoorWidth();

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



    public int exitCountDoor() {
        List<Room> room = randomRoomsIsMap();
        return 0;
    }


}
