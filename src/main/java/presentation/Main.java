package presentation;

import domain.characters.Player;
import domain.characters.enemies.Zombie;
import domain.gameSession.Game;
import domain.map.Corridor;
import domain.map.DungeonGenerator;
import domain.map.Room;
import domain.navigator.MovementSystem;
import domain.navigator.Position;

public class Main {
    public static void main(String[] args) {
        Player player = new Player(new Position(80, 40));

//        System.out.println(room + "\n");
//        System.out.println("room up: " + (room.getPosition().getY() + 1) + " player x: " + (player.getPosition().getY()));
//        System.out.println("room down: " + (room.getPosition().getY() + room.getHeight() - 1));
//        System.out.println("room left: " + (room.getPosition().getX() + 1));
//        System.out.println("room reight: " + (room.getPosition().getX() + room.getWidth() - 1));
//        MovementSystem mv = new MovementSystem();
//
//        boolean check = mv.characterOutsideBorder(player, room);
//        System.out.println(check);
//        Room room1 = new Room(0, 0);
//        Room room2 = new Room(0, 1);
//        Room room3 = new Room( 0, 2);
//        Corridor corridor = new Corridor(room1, room2);
//        System.out.println(room1 + "\n");
//        System.out.println(room2 + "\n");
//        System.out.println(corridor.getPath());
//
//        System.out.println(corridor.intersectsRoom(room3));

//        DungeonGenerator generator = new DungeonGenerator(45, 45);
//
//        generator.generateDungeon();
        Game game = new Game();
        game.gen();
    }
}