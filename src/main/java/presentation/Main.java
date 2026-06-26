package presentation;

import domain.characters.Player;
import domain.characters.enemies.Zombie;
import domain.navigator.Position;

public class Main {
    public static void main(String[] args) {

        Player player = new Player(new Position(0, 0));
        Zombie zombie = new Zombie(new Position(0, 2));

        System.out.println(zombie.convergenceIsHostility(player));

    }
}