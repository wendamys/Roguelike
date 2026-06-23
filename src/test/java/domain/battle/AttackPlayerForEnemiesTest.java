package domain.battle;

import domain.characters.Player;
import domain.characters.enemies.*;
import domain.navigator.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static domain.battle.CharacterType.PLAYER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttackPlayerForEnemiesTest {
    private Player player;
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
        player = new Player(new Position(0, 0));
        vampire = new Vampire(new Position(0, 0));
        snake = new Snake(new Position(0, 0));
        zombie = new Zombie(new Position(0, 0));
        ogre = new Ogre(new Position(0, 0));
        ghost = new Ghost(new Position(0, 0));
        mimic = new Mimic(new Position(0, 0));
    }

    @Test
    void AttackPlayerForVampire() {
        System.out.println("Player");
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, vampire, PLAYER, battleInfo);
        }
        assertEquals(0, vampire.getHealth());
    }

    @Test
    void AttackPlayerForOgre() {
        System.out.println("Player");
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, ogre, PLAYER, battleInfo);
        }
        assertEquals(0, ogre.getHealth());
    }

    @Test
    void AttackPlayerForZombie() {
        System.out.println("Player");
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, zombie, PLAYER, battleInfo);
        }
        assertEquals(0, zombie.getHealth());
    }

    @Test
    void AttackPlayerForGhost() {
        System.out.println("Player");
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, ghost, PLAYER, battleInfo);
        }
        assertEquals(0, ghost.getHealth());
    }

    @Test
    void AttackPlayerForSnake() {
        System.out.println("Player");
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, snake, PLAYER, battleInfo);
        }
        assertEquals(0, snake.getHealth());
    }

    @Test
    void AttackPlayerForMimic() {
        System.out.println("Player");
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, mimic, PLAYER, battleInfo);
        }
        assertEquals(0, mimic.getHealth());
    }
}
