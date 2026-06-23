package domain.battle;

import domain.characters.Player;
import domain.characters.enemies.*;
import domain.navigator.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static domain.battle.CharacterType.ENEMIES;

public class AttackEnemiesForPlayerTest {
    private Vampire vampire;
    private Snake snake;
    private Zombie zombie;
    private Ogre ogre;
    private Ghost ghost;
    private Mimic mimic;
    BattleInfoType battleInfo = new BattleInfoType();
    AttackSystem atk = new AttackSystem();

    @BeforeEach
    void setUp() {
        vampire = new Vampire(new Position(0,0));
        snake = new Snake(new Position(0,0));
        zombie = new Zombie(new Position(0,0));
        ogre = new Ogre(new Position(0,0));
        ghost = new Ghost(new Position(0,0));
        mimic = new Mimic(new Position(0 ,0));
    }

    @Test
    void AttackVampireForPlayer() {
        Player player = new Player(new Position(0, 0));
        System.out.println(vampire.getType());
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, vampire, ENEMIES, battleInfo);
            if (player.getHealth() == 0) { return; }
        }
        assertEquals(0, player.getHealth());
    }

    @Test
    void AttackZombieForPlayer() {
        System.out.println(zombie.getType());
        Player player = new Player(new Position(0, 0));
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, zombie, ENEMIES, battleInfo);
            if (player.getHealth() == 0) { return; }
        }
        assertEquals(0, player.getHealth());
    }

    @Test
    void AttackSnakeForPlayer() {
        System.out.println(snake.getType());
        Player player = new Player(new Position(0, 0));
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, snake, ENEMIES, battleInfo);
            if (player.getHealth() == 0) { return; }
        }
        assertEquals(0, player.getHealth());
    }

    @Test
    void AttackOgreForPlayer() {
        System.out.println(ogre.getType());
        Player player = new Player(new Position(0, 0));
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, ogre, ENEMIES, battleInfo);
            if (player.getHealth() == 0) { return; }
        }
        assertEquals(0, player.getHealth());
    }

    @Test
    void AttackGhostForPlayer() {
        System.out.println(ghost.getType());
        Player player = new Player(new Position(0, 0));
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, ghost, ENEMIES, battleInfo);
            if (player.getHealth() == 0) { return; }
        }
        assertEquals(0, player.getHealth());
    }

    @Test
    void AttackMimicForPlayer() {
        System.out.println(mimic.getType());
        Player player = new Player(new Position(0, 0));
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, mimic, ENEMIES, battleInfo);
            if (player.getHealth() == 500 && i == 10) { return; }
        }
        assertEquals(500, player.getHealth());
    }
}
