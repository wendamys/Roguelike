package domain.battle;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.*;
import domain.map.Level;
import domain.navigator.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static domain.battle.CharacterType.PLAYER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttackPlayerForEnemiesTest {
    BattleInfoType battleInfo = new BattleInfoType();
    AttackSystem atk = new AttackSystem();

    static Stream<Enemies> enemyProvider() {
        return Stream.of(
                new Vampire(new Position(0, 0)),
                new Snake(new Position(0, 0)),
                new Zombie(new Position(0, 0)),
                new Ogre(new Position(0, 0)),
                new Ghost(new Position(0, 0)),
                new Mimic(new Position(0, 0))
        );
    }

    @ParameterizedTest
    @MethodSource("enemyProvider")
    void AttackPlayerForEnemies(Enemies enemy) {
        Player player = new Player(new Position(0, 0));
        // System.out.println("\nPlayer vs " + enemy.getType());
        for (int i = 0; 1000 > i; i++) {
            atk.attack(player, enemy, PLAYER, battleInfo);
            if (enemy.getHealth() == 0) {
                break;
            }
        }
        assertEquals(0, enemy.getHealth());
    }
}
