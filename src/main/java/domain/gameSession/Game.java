package domain.gameSession;

import domain.characters.Player;
import domain.map.Corridor;
import domain.map.DungeonGenerator;
import domain.map.Room;

import java.util.List;

public class Game {

    public void generateMap() {
        DungeonGenerator generator = new DungeonGenerator(70, 60);
        generator.generateDungeon();
        List<Room> rooms = generator.getRooms();
        List<Corridor> corridors = generator.getCorridors();
        Player player = new Player(rooms.getFirst().getCentreRoom());
        generator.createPlayer(player);
        for(var room : rooms) {
            if(room != rooms.getFirst()) {
                generator.createItem(room);
                generator.createEnemies(room);
            }
        }
        generator.printMap();
    }
}