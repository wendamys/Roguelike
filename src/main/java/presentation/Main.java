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
        Player player = new Player(new Position(20, 20));
        Game game = new Game();
        game.generateMap();
    }
}