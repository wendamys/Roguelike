package domain.gameSession;

import domain.characters.Player;
import domain.map.Corridor;
import domain.map.DungeonGenerator;
import domain.map.Room;

import java.util.List;

public class Game {
    // Создаем генератор подземелья

    public void gen() {
        DungeonGenerator generator = new DungeonGenerator(80, 40);

        // Генерируем подземелье
        generator.generateDungeon();

        // Выводим карту
        generator.printMap();

        // Получаем комнаты и коридоры
        List<Room> rooms = generator.getRooms();
        List<Corridor> corridors = generator.getCorridors();
    }




}
