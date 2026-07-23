package domain.battle;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.*;
import domain.navigator.Position;
import domain.backpack.Backpack;
import org.junit.jupiter.api.Test;

import java.util.List;

import static domain.battle.CharacterType.PLAYER;

public class TestDebug {
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

    @Test
    void testSingleEnemy() {
        Player player = new Player(new Position(0, 0));
        Enemies enemy = new Zombie(new Position(0, 0), null);
        
        System.out.println("Initial enemy health: " + enemy.getHealth());
        System.out.println("Player strength: " + player.getStrength());
        System.out.println("Backpack weapon power: " + backpack.getWeaponPower());
        
        for (int i = 0; i < 10; i++) {
            atk.attack(player, enemy, PLAYER, battleInfo, backpack);
            System.out.println("After attack " + (i+1) + ": enemy health = " + enemy.getHealth() + ", player gold = " + player.getGold());
            if (enemy.getHealth() == 0) { 
                System.out.println("Enemy defeated!");
                break; 
            }
        }
    }
}
