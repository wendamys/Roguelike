package domain.navigator;

import domain.characters.Player;
import domain.characters.enemies.Zombie;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnemiesConvergenceTest {



    @Test
    void convergenceTest() {
        MovementSystem mv = new MovementSystem();

        Zombie zombie = new Zombie(new Position(0, 0));
        Player player = new Player(new Position(0, 1));

        DirectionType dt = zombie.convergence(player);
        mv.moveDir(dt, zombie);
        zombie.convergence(player);

        assertEquals(1, zombie.getPosition().getY());

    }

}
