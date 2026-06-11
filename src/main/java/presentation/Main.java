package presentation;


import domain.characters.player.Player;
import domain.navigator.Direction;
import domain.navigator.ImmutableDistance;
import domain.navigator.ImmutablePosition;

public class Main {
    public static void main(String[] args) {
        ImmutablePosition position = new ImmutablePosition(0, 0);
        Player player = new Player("wqer", 100, 100,100, true,100,0, position);
        System.out.println(player.getPosition() + "main");

        ImmutablePosition position1 = player.move(Direction.FORWARD, 1);
        System.out.println(position1 + "main");
        ImmutablePosition position2 = player.move(Direction.FORWARD, 1);
        ImmutablePosition position3 = player.move(Direction.FORWARD, 1);
        ImmutablePosition position4 = player.move(Direction.FORWARD, 1);
        ImmutablePosition position5 = player.move(Direction.RIGHT, 1);

//        System.out.println(player.move(Direction.FORWARD, 1) + "main");
        System.out.println(position5 + "player");

        Player player1 = new Player("qwe", 100, 100, 100, true, 100, 0, position);
        System.out.println(player1.getPosition() + "player1");
        ImmutableDistance distance = new ImmutableDistance(0, 0);
        double range = distance.distanceTo(player.getPosition());
        System.out.println(range);
        int damage = 50;
        player1.acceptDamage(damage);
        System.out.println(player1.getHealth());



    }
}