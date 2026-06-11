package presentation;


import domain.characters.player.Player;
import domain.navigator.Direction;
import domain.navigator.ImmutablePosition;

public class Main {
    public static void main(String[] args) {
        ImmutablePosition position = new ImmutablePosition(0, 0);
        Player player = new Player("wqer", 100, 100,100, true,100,0, position);
        System.out.println(player.getPosition());
        player.playerMove(Direction.FORWARD);
        System.out.println(player.getPosition());
        player.playerMove(Direction.DOWN);
        System.out.println(player.getPosition());
        player.playerMove(Direction.LEFT);
        System.out.println(player.getPosition());

        for (int i = 0; i < 10; i++) {
            player.playerMove(Direction.RIGHT);
        }
        System.out.println(player.getPosition());
    }
}