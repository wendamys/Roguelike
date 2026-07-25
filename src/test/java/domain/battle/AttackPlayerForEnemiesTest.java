package domain.battle;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.*;
import domain.gameSession.DifficultyType;
import domain.navigator.Position;
import domain.backpack.Backpack;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static domain.battle.CharacterType.PLAYER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttackPlayerForEnemiesTest {
    BattleInfoType battleInfo = new BattleInfoType();
    AttackSystem atk = new AttackSystem();
    Backpack backpack = new Backpack();

    static Stream<Enemies> enemyProvider() {
        return Stream.of(
                new Vampire(new Position(0, 0), DifficultyType.EASY),
                new Snake(new Position(0, 0), DifficultyType.EASY),
                new Zombie(new Position(0, 0), DifficultyType.EASY),
                new Ogre(new Position(0, 0), DifficultyType.EASY),
                new Ghost(new Position(0, 0), DifficultyType.EASY),
                new Mimic(new Position(0, 0), DifficultyType.EASY)
        );
    }

    @ParameterizedTest
    @MethodSource("enemyProvider")
    void AttackPlayerForEnemies(Enemies enemy) {
        Player player = new Player(new Position(0, 0));
        // System.out.println("\nPlayer vs " + enemy.getType());
        for (int i = 0; 1000 > i; i++) {
            atk.attack(player, enemy, PLAYER, battleInfo, backpack);
            if (enemy.getHealth() == 0) {
                break;
            }
        }
        assertEquals(0, enemy.getHealth());
    }
}
