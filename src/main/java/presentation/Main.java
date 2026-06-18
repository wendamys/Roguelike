package presentation;


import domain.MathUtils.RandomNumber;
import domain.battle.AttackSystem;
import domain.battle.BattleInfoType;
import domain.battle.CharacterType;
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


public class Main {
    public static void main(String[] args) {
        Player player = new Player("weer", 100, 100, 100, 100, 0, new Position(0, 0));
        MovementSystem mv = new MovementSystem();
        Player player2 = new Player("weer", 100, 100, 100, 100, 0, new Position(0, 0));

        for (int i = 0; i < 5; i++) {
           mv.moveDir(DirectionType.FORWARD, player);
            System.out.println("player " + player.getPosition().getX()+  ", " + player.getPosition().getY());
        }
        mv.moveRandom(player2);
        System.out.println("player2 " + player2.getPosition().getX()+  ", " + player2.getPosition().getY());

        Zombie zombie = new Zombie("Zombie", EnemiesType.ZOMBIE, 100, 100, 100, 100, new Position(3, 7));
        for (int i = 0; i < 5; i++) {
            // Возвращается лучшее направление
            //DirectionType bestTypeDir = zombie.getPosition().convergence(player);

            DirectionType bestTypeDir = zombie.getPosition().convergence(player);

            //Можно сразу запихнуть в перемещение, но тут должна быть скорее всего еще одна функция,
            // которая будет сравнивать координаты до атаки т.е. брать координаты и сравнивать и т.д.,
            // это уже другая история
            //mv.moveDir(zombie.getPosition().convergence(player), zombie);

            mv.moveDir(zombie.getPosition().convergence(player), zombie);
            System.out.println("Zombie pos x:" + zombie.getPosition().getX() + " Zombie pos y:" + zombie.getPosition().getY());
        }

        Position position = new Position(0, 0);
        Backpack backpack = new Backpack();
        AttackSystem atk = new AttackSystem();
        Elixir elixir = new Elixir("E", ItemsType.ELIXIR, 30, position);
        Elixir elixir2 = new Elixir("E", ItemsType.ELIXIR, 10, position);
        backpack.takeItem(elixir);
        backpack.takeItem(elixir2);
        System.out.print("Be: ");
        backpack.seeList(ItemsType.ELIXIR);
        backpack.clearLists();
        System.out.println("posle: ");
        backpack.seeList(ItemsType.ELIXIR);

        System.out.println(zombie.getHealth());
        atk.acceptDamage(10, zombie);
        System.out.println(zombie.getHealth());

        BattleInfoType battleInfo = new BattleInfoType();
        atk.attack(player, battleInfo, CharacterType.ENEMIES);
        System.out.println(player.getHealth());

    }
}
