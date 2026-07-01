package presentation;

import domain.map.Door;
import domain.map.Room;

public class Main {
    public static void main(String[] args) {

        Room room = new Room();
        Door door = new Door(room);
        System.out.println(room);
        System.out.println("Ширина комнаты: " + room.getWidth() + " Высота комнаты: " + room.getHeight());
        System.out.println();
        System.out.println(door.randomPositionDoorWidth());
        System.out.println(door.randomPositionDoorHeight());

        door.randomRoomsIsMap();
        int i = 0;
        for (Integer num: door.randomCountDoorIsRoom()) {
            System.out.println("В комнате " + i + " Дверей " + num);
            i++;
        }
//        System.out.println(door.numberDoorIsRoom());
//        System.out.println(door.generationDoorIsRoom());

    }
}