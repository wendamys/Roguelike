package domain.battle;

import domain.characters.Player;
import domain.characters.enemies.*;
import domain.navigator.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static domain.battle.CharacterType.PLAYER;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TakeGoldPlayerForEnemiesTest {
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
        vampire = new Vampire(new Position(0, 0));
        snake = new Snake(new Position(0, 0));
        zombie = new Zombie(new Position(0, 0));
        ogre = new Ogre(new Position(0, 0));
        ghost = new Ghost(new Position(0, 0));
        mimic = new Mimic(new Position(0, 0));
    }

    @Test
    void TakeGoldPlayerForVampire() {
        Player player = new Player(new Position(0, 0));
        System.out.println("Player");
        for (int i = 0; 100 > i; i++) {
            atk.attack(player, vampire, PLAYER, battleInfo);
        }
        assertEquals(player.getGold() > 0, true);
    }
}