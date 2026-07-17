package presentation;

import datalayer.DataLayer;
import domain.backpack.Item;
import domain.backpack.items.Elixir;
import domain.gameSession.Game;
import domain.navigator.Position;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        Game game = new Game();
//        game.start();
        game.getPlayer().setName("qwe");
        game.getBackpack().takeItem(new Elixir(new Position(0, 0)));

        DataLayer.save(game);

    }
}