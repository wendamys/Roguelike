package presentation;

import datalayer.DataLayer;
import domain.backpack.Item;
import domain.backpack.items.Elixir;
import domain.gameSession.Game;
import domain.map.DungeonGenerator;
import domain.map.Room;
import domain.navigator.Position;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        Game game = new Game();
        DataLayer.save(game);
    }
}