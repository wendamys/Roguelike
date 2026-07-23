package domain.battle;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.*;
import domain.navigator.Position;
import domain.backpack.Backpack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static domain.battle.CharacterType.PLAYER;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TakeGoldPlayerForEnemiesTest {
    BattleInfoType battleInfo = new BattleInfoType();
    AttackSystem atk = new AttackSystem();
    Backpack backpack = new Backpack();

    private static List<Enemies> getEnemyList() {
        return List.of(
            new Vampire(new Position(0, 0), null),
            new Snake(new Position(0, 0), null),
            new Zombie(new Position(0, 0), null),
            new Ogre(new Position(0, 0), null),
            new Ghost(new Position(0, 0), null),
            new Mimic(new Position(0, 0), null)
        );
    }

    static Stream<Enemies> enemyProvider() {
        return getEnemyList().stream();
    }

    @ParameterizedTest
    @MethodSource("enemyProvider")
    void TakeGoldPlayerForEnemy(Enemies enemy) {
        Player player = new Player(new Position(0, 0));
        // System.out.println("\nPlayer take gold vs " + enemy.getType());
        for (int i = 0; 1000 > i; i++) {
                atk.attack(player, enemy, PLAYER, battleInfo, backpack);
            if (enemy.getHealth() == 0) { break; }
        }
        assertTrue(player.getGold() > 0);
    }

    @Test
    void TakeGoldPlayerForAllEnemy() {
        Player player = new Player(new Position(0, 0));
        for (Enemies enemy: getEnemyList()) {
            // System.out.println("\nPlayer take gold vs " + enemy.getType());
            for (int i = 0; 1000 > i; i++) {
                atk.attack(player, enemy, PLAYER, battleInfo, backpack);
                if (enemy.getHealth() == 0) { break; }
            }
        }
        assertTrue(player.getGold() > 40);
    }
}
