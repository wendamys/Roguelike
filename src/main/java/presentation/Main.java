package presentation;


import domain.MathUtils.RandomNumber;
import domain.battle.AttackSystem;
import domain.battle.BattleInfoType;
import domain.battle.CharacterType;
import domain.characters.Enemies;
import domain.navigator.MovementSystem;
import domain.backpack.Backpack;
import domain.backpack.ItemsType;
import domain.backpack.items.Elixir;
import domain.characters.enemies.EnemiesType;
import domain.characters.enemies.Zombie;
import domain.characters.Player;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import static domain.MathUtils.RandomNumber.*;
import static domain.battle.CharacterType.*;
import static domain.characters.enemies.EnemiesType.*;


public class Main {
    public static void main(String[] args) {
        Player player = new Player("weer", 100, 100, 100, 100, 0, new Position(0, 0));
        MovementSystem mv = new MovementSystem();

//        for (int i = 0; i < 5; i++) {
//           mv.moveDir(DirectionType.FORWARD, player);
//            System.out.println("player " + player.getPosition().getX()+  ", " + player.getPosition().getY());
//        }
        Zombie zombie = new Zombie("Zombie", 100, 100, 70, 100, ZOMBIE, new Position(3, 7));
//        for (int i = 0; i < 5; i++) {
//            // Возвращается лучшее направление
//            //DirectionType bestTypeDir = zombie.getPosition().convergence(player);
//
//            DirectionType bestTypeDir = zombie.getPosition().convergence(player);
//
//            //Можно сразу запихнуть в перемещение, но тут должна быть скорее всего еще одна функция,
//            // которая будет сравнивать координаты до атаки т.е. брать координаты и сравнивать и т.д.,
//            // это уже другая история
//            //mv.moveDir(zombie.getPosition().convergence(player), zombie);
//
//            mv.moveDir(zombie.getPosition().convergence(player), zombie);
//            System.out.println("Zombie pos x:" + zombie.getPosition().getX() + " Zombie pos y:" + zombie.getPosition().getY());
//        }

//        Position position = new Position(0, 0);
//        Backpack backpack = new Backpack();
        AttackSystem atk = new AttackSystem();

        atk.checkHit(player, zombie, PLAYER);
        atk.checkHit(player, zombie, ENEMIES);

        //System.out.println(zombie.getHealth());
        //atk.acceptDamage(10, zombie);
        //System.out.println(zombie.getHealth());
        //BattleInfoType battleInfo = new BattleInfoType();
        //atk.attack(player, battleInfo, CharacterType.ENEMIES);
        //System.out.println(atk.checkHit(player, zombie, PLAYER));
        //System.out.println(atk.checkHit(zombie, player, CharacterType.ENEMIES));

    }
}
