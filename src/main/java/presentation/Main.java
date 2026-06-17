package presentation;


import domain.characters.enemies.EnemiesType;
import domain.characters.enemies.Zombie;
import domain.characters.Player;
import domain.navigator.DirectionType;
import domain.navigator.Position;


public class Main {
    public static void main(String[] args) {
        Position position = new Position(0, 0);
        Player player = new Player("weer", 100, 100, 100, 100, 0, position);
        System.out.println(player.getPosition().getX() + " " + position.getY() + "main");

        player.move(DirectionType.FORWARD);

        for (int i = 0; i < 5; i++) {
            player.move(DirectionType.RIGHT);
        }
        System.out.println(player.getPosition() + " main");

        Zombie zombie = new Zombie("Zombie", EnemiesType.ZOMBIE, 100, 100, 100, 100, position);
        for (int i = 0; i < 5; i++) {
            zombie.moveRandom(1);
            System.out.println(zombie.getPosition().getX() + " " + zombie.getPosition().getY());
        }
        for (int i = 0; i < 20; i++) {
            boolean attack = zombie.convergence(player.getPosition(), 1);
            if (attack) {
                zombie.attack(player);
                System.out.println(player.getHealth());
            }
            System.out.println("Zombie pos x:" + zombie.getPosition().getX() + " Zombie pos y:" + zombie.getPosition().getY());
        }
    }
}