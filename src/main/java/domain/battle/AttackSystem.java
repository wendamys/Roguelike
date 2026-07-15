package domain.battle;

import domain.ai.StunAI;
import domain.ai.DebuffAI;
import domain.ai.RegenAI;
import domain.backpack.Backpack;
import domain.characters.Character;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.*;

import static domain.MathUtils.MathUtils.randomNumber;
import static domain.MathUtils.MathUtils.randomValueDouble;
import static domain.battle.CharacterType.ENEMIES;
import static domain.battle.CharacterType.PLAYER;

public class AttackSystem {

    /**
     * Функция, высчитывающая, произойдёт ли попадание
     *
     * @param enemy      Данные о монстре
     * @param battleInfo Данные о бое
     */
    public static int EnemyDamageFormula(Enemies enemy, BattleInfoType battleInfo) {
        int damage = 0;
        switch (enemy.getType()) {
            case ZOMBIE, GHOST, MIMIC -> damage = zombieGhostDamageFormula(enemy);
            case OGRE -> damage = ogreDamageFormula(enemy, battleInfo);
            case SNAKE -> damage = snakeDamageFormula(enemy, battleInfo);
            case VAMPIRE -> damage = vampireDamageFormula(enemy);
        }
        return damage;
    }

    /**
     * Функция вычисления урона зомби и призрака
     *
     * @return Количество урона, наносимое монстром игроку
     */
    static int zombieGhostDamageFormula(Enemies enemies) {
        return enemies.getStrength();
    }

    /**
     * Функция вычисления урона огра
     * Огр атакует раз в два хода, при этом гарантированно попадая в игрока
     *
     * @return Количество урона, наносимое монстром игроку
     */
    static int ogreDamageFormula(Enemies enemy, BattleInfoType battle_info) {
        int damage = 0;
        if (!battle_info.ogreCoolDown) damage = enemy.getStrength();
        else battle_info.ogreCoolDown = false;
        return damage;
    }

    /**
     * Функция вычисления урона змеи
     * Змея с шансом 15 процентов может усыпить игрока на один ход
     *
     * @return Количество урона, наносимое монстром игроку
     */
    static int snakeDamageFormula(Enemies enemy, BattleInfoType battle_info) {
        if (randomNumber(0, 100) <= 15) {
            battle_info.playerAsSleep = true;
        }
        return zombieGhostDamageFormula(enemy);
    }

    /**
     * Функция вычисления урона вампира
     * Вычисляет базовый урон вампира
     *
     * @return Количество урона, наносимое монстром игроку
     */
    static int vampireDamageFormula(Enemies enemy) {
        return (int)( enemy.getStrength() * 0.5);
    }

    /**
     * Функция, описывающая попытку атаки
     * Эта функция объединяет в себе всю структуру атаки: проверка на попадание, расчет урона, нанесение урона.
     *
     * @param player     Данные об игроке
     * @param enemy      Данные об монстре
     * @param battleInfo Данные о бое
     * @param currTurn   Определяет, чья очередь выполнить атаку
     * @param backpack   Рюкзак игрока
     */
    public void attack(Player player, Enemies enemy, CharacterType currTurn, BattleInfoType battleInfo, Backpack backpack) {
        switch (currTurn) {
            case PLAYER -> {
                if (enemy.getHealth() == 0) return;
                
                // Проверка на попадание
                if (checkHit(player, enemy, PLAYER, battleInfo)) {
                    int damage = calculateDamage(player, enemy, PLAYER, battleInfo, backpack);
                    int newHealth = Math.max(enemy.getHealth() - damage, 0);
                    System.out.println("у ENEMIES "  + "Было hp: " + enemy.getHealth() + " Стало: " + newHealth);
                    enemy.setHealth(newHealth);
                }
                
                if (enemy.getHealth() == 0) {
                    player.setGold(player.getGold() + calculateLoot(enemy));
                    System.out.println("Голда у игрока: " + player.getGold());
                }
            }
            case ENEMIES -> {
                if (player.getHealth() == 0) return;
                
                // Проверка на попадание
                if (checkHit(player, enemy, ENEMIES, battleInfo)) {
                    int damage = calculateDamage(player, enemy, ENEMIES, battleInfo, backpack);
                    int newHealth = Math.max(player.getHealth() - damage, 0);
                    System.out.println("у PLAYER "  + "Было hp: " + player.getHealth() + " Стало: " + newHealth);
                    player.setHealth(newHealth);
                    
                    // Стан от огра
                    if (enemy.getType() == EnemiesType.OGRE) {
                        StunAI stunAI = ((Ogre) enemy).getStunAI();
                        if (stunAI.tryStun()) {
                            battleInfo.isStunned = true;
                            player.setStunned(true);
                            System.out.println("Игрок в стане!");
                        }
                    }
                    
                    // Дебаф от змеи
                    if (enemy.getType() == EnemiesType.SNAKE) {
                        DebuffAI debuffAI = ((Snake) enemy).getDebuffAI();
                        if (debuffAI.tryApplyDebuff()) {
                            battleInfo.isMissNextAttack = true;
                            System.out.println("Игрок получил дебаф промаха!");
                        }
                    }
                    
                    // Восстановление здоровья вампира
                    if (enemy.getType() == EnemiesType.VAMPIRE && damage > 0) {
                        regenVampire(enemy, damage);
                    }
                }
            }
        }
    }
    
    /**
     * Восстановление здоровья вампира после атаки
     * @param enemy вампир
     * @param damage урон, нанесенный игроку
     */
    private void regenVampire(Enemies enemy, int damage) {
        if (enemy instanceof Vampire) {
            RegenAI regenAI = ((Vampire) enemy).getRegenAI();
            int regenAmount = regenAI.calculateRegen(damage);
            int newHealth = Math.min(enemy.getHealth() + regenAmount, enemy.getMaxHealth());
            enemy.setHealth(newHealth);
            System.out.println("Vampire восстановил " + regenAmount + " здоровья");
        }
    }

    /**
     * Функция, высчитывающая, произойдёт ли попадание
     *
     * @param currTurn Определяет, чья очередь выполнить атаку
     * @param enemy    Данные о монстре
     * @param player   Данные об игроке
     * @param battleInfo Данные о бое
     */
    public boolean checkHit(Character player, Enemies enemy, CharacterType currTurn, BattleInfoType battleInfo) {
        boolean wasHit = false;
        int chance = 0;
        switch (currTurn) {
            case PLAYER -> {
                // Если игрок получил дебаф промаха
                if (battleInfo.isMissNextAttack) {
                    battleInfo.isMissNextAttack = false;
                    System.out.println("Игрок промахнулся (дебаф)!");
                    return false;
                }
                chance = hitChanceFormula(player.getAgility(), enemy.getAgility());
            }
            case ENEMIES -> {
                // Если Ghost в инвизе, промах
                if (enemy.getType() == EnemiesType.GHOST) {
                    if (enemy.getIsInvisible()) {
                        System.out.println("Ghost невидим, промах!");
                        return false;
                    }
                }
                chance = hitChanceFormula(enemy.getAgility(), player.getAgility());
            }
        }
        wasHit = randomValueDouble() * 100 < chance;
        return wasHit;
    }

    /**
     * Функция, высчитывающая шанс попадания
     *
     * @param agilityAttacker Agile атакующего
     * @param agilityEnemy    Agile врага
     * @return Шанс попадания в процентах
     */
    static int hitChanceFormula(int agilityAttacker, int agilityEnemy) {
        int hitChance;
        if (agilityAttacker >= agilityEnemy) {
            hitChance = 85 + (int) ((agilityAttacker - agilityEnemy) * 0.2);
            if (hitChance > 95) hitChance = 95;
        } else {
            hitChance = 85 - (int) ((agilityEnemy - agilityAttacker) * 0.25);
            if (hitChance < 5) hitChance = 5;
        }
        return hitChance;
    }

    /**
     * Функция, высчитывающая урон, наносимый игроком
     *
     * @param player     Данные об игроке
     * @param enemy      Данные о монстре
     * @param currTurn   Определяет, чья очередь выполнить атаку
     * @param battleInfo Данные о бое
     * @param backpack   Рюкзак игрока
     * @return Количество урона, наносимое игроком монстру
     */
    static int calculateDamage(Player player, Enemies enemy, CharacterType currTurn, BattleInfoType battleInfo, Backpack backpack) {
        int damage = 0;
        if (currTurn == PLAYER) {
            int weaponPower = backpack.getWeaponPower();
            damage = (int) ((player.getStrength() + randomNumber(0, 5)) * (1 + (double) weaponPower / 100));
            // Критический удар
            if (randomValueDouble() < 0.1) {
                damage = (int) (damage * 1.5);
                System.out.println("КРИТИЧЕСКИЙ УДАР!");
            }
        } else if (currTurn == ENEMIES) {
            damage = EnemyDamageFormula(enemy, battleInfo);
            // ВАМПИР: атакует дважды при первой атаке
            if (enemy.getType() == EnemiesType.VAMPIRE && battleInfo.vampireFirstAttack) {
                battleInfo.vampireFirstAttack = false;
                damage = damage * 2;
                System.out.println("Vampire двойная атака!");
            }
        }
        return damage;
    }

    /**
     * Функция, высчитывающая количество золота, выпадающее из монстра
     *
     * @param enemy Монстр
     * @return Количество золота
     */
    static int calculateLoot(Enemies enemy) {
        int gold = 0;
        switch (enemy.getType()) {
            case ZOMBIE -> gold = randomNumber(1, 5);
            case OGRE -> gold = randomNumber(5, 10);
            case SNAKE -> gold = randomNumber(3, 8);
            case VAMPIRE -> gold = randomNumber(8, 15);
            case GHOST -> gold = randomNumber(5, 12);
            case MIMIC -> gold = randomNumber(15, 30);
        }
        return gold;
    }
}
