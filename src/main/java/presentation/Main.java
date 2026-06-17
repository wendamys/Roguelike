package presentation;


import domain.navigator.MovementSystem;
import domain.backpack.Backpack;
import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.backpack.items.Elixir;
import domain.characters.enemies.EnemiesType;
import domain.characters.enemies.Zombie;
import domain.characters.Player;
import domain.navigator.DirectionType;
import domain.navigator.Position;


public class Main {
    public static void main(String[] args) {

        Player player = new Player("weer", 100, 100, 100, 100, 0, new Position(0, 0));
        MovementSystem mv = new MovementSystem();
        Player player2 = new Player("weer", 100, 100, 100, 100, 0, new Position(0, 0));


        for (int i = 0; i < 5; i++) {
           mv.moveDir(DirectionType.FORWARD, player);
            System.out.println("player " + player.getPosition().getX()+  ", " + player.getPosition().getY());
        }
        mv.moveRandom(player2);
        System.out.println("player2 " + player2.getPosition().getX()+  ", " + player2.getPosition().getY());
        Position position = new Position(0, 0);
        System.out.println(player.getPosition().getX() + " " + position.getY() + " player");
        Backpack backpack = new Backpack();
        Elixir elixir = new Elixir("E", ItemsType.ELIXIR, 30, position);
        backpack.takeItem(elixir);
        backpack.seeList();

        Zombie zombie = new Zombie("Zombie", EnemiesType.ZOMBIE, 100, 100, 100, 100, new Position(3, 7));
        for (int i = 0; i < 5; i++) {
            DirectionType bestTypeDir = zombie.getPosition().convergence(player);
            mv.moveDir(bestTypeDir, zombie);
            System.out.println("Zombie pos x:" + zombie.getPosition().getX() + " Zombie pos y:" + zombie.getPosition().getY());
        }
    }
}