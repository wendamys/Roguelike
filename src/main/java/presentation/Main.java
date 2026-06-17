package presentation;


import domain.backpack.Backpack;
import domain.backpack.ItemsType;
import domain.backpack.items.Elixir;
import domain.characters.Player;
import domain.navigator.Position;


public class Main {
    public static void main(String[] args) {
        Position position = new Position(0, 0);
        Player player = new Player("weer", 100, 100, 100, 100, 0, position);
        Backpack backpack = new Backpack();
        Elixir elixir = new Elixir("E", ItemsType.ELIXIR, 30, position);
        Elixir elixir2 = new Elixir("E", ItemsType.ELIXIR, 10, position);
        backpack.takeItem(elixir);
        backpack.takeItem(elixir2);
        backpack.seeList(ItemsType.ELIXIR);
    }
}