package domain.battle;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.*;
import domain.map.Level;
import domain.navigator.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static domain.battle.CharacterType.ENEMIES;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttackEnemiesForPlayerTest {

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
    void AttackEnemyForPlayer(Enemies enemy) {
        Player player = new Player(new Position(0, 0));
        System.out.println("\n" + enemy.getType() + " vs Player");
        for (int i = 0; 1000 > i; i++) {
            atk.attack(player, enemy, ENEMIES, battleInfo);
            if (player.getHealth() == 0) {
                break;
            }
        }
        assertEquals(0, player.getHealth());
    }
}
