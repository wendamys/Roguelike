package presentation;


import domain.characters.enemies.EnemiesType;
import domain.characters.enemies.Zombie;
import domain.navigator.MovementSystem;
import domain.characters.Player;
import domain.navigator.DirectionType;
import domain.navigator.Position;


public class Main {
    public static void main(String[] args) {

        Player player = new Player("weer", 100, 100, 100, 100, 0, new Position(4, 5));
        MovementSystem mv = new MovementSystem();

        Zombie zombie = new Zombie("Zombie", EnemiesType.ZOMBIE, 100, 100, 100, 100, new Position(3, 7));
        for (int i = 0; i < 5; i++) {
            DirectionType bestTypeDir = zombie.getPosition().convergence(player);
            mv.moveDir(bestTypeDir, zombie);
            System.out.println("Zombie pos x:" + zombie.getPosition().getX() + " Zombie pos y:" + zombie.getPosition().getY());
        }
    }
}