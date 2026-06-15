package presentation;


import domain.characters.enemies.EnemiesType;
import domain.characters.enemies.Zombie;
import domain.characters.player.Player;
import domain.navigator.DirectionType;
import domain.navigator.Position;


public class Main {
    public static void main(String[] args) {
        Position position = new Position(0, 0);
        Player player = new Player("wqer", 100, 100,100, 100,0, position);
        System.out.println(player.getPosition() + "main");

        player.move(DirectionType.FORWARD, 1);

        for (int i = 0; i < 10; i++) {
            player.move(DirectionType.RIGHT, 1);
        }
        System.out.println(player.getPosition() + "main");
//
//        Player player1 = new Player("qwe", 100, 100, 100, 100, 0, position);
//        System.out.println(player1.getPosition() + "player1");
//        ImmutableDistance distance = new ImmutableDistance(0 , 1);
//        double range = distance.distanceTo(player.getPosition());
//        System.out.println(range + " range");
//        int damage = 20;
//        player1.acceptDamage(damage);
//        System.out.println(player1.getHealth() + " health");

        Zombie zombie = new Zombie("Zombie", EnemiesType.ZOMBIE, 100,100, 100, 100, position);
        for(int i = 0; i < 10; i++) {
            zombie.moveRandom(1);
            System.out.println(zombie.getPosition().getX() + " " + zombie.getPosition().getY());
        }
            for(int i = 0; i < 15; i++) {
                zombie.convergence(player.getPosition(), 1);
                System.out.println("Zombi pos x:" + zombie.getPosition().getX() + " Zombi pos y:" + zombie.getPosition().getY());
        }
    }
}