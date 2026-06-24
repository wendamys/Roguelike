package domain.battle;

import domain.characters.Character;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.EnemiesType;
import domain.characters.enemies.Vampire;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import java.util.ArrayList;

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
        if (!battle_info.ogreCoolDown) {
            damage = enemy.getStrength();
        } else {
            battle_info.ogreCoolDown = false;
        }
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
            // System.out.println("Игрок спит!");
            battle_info.playerAsSleep = true;
        }
        return zombieGhostDamageFormula(enemy);
    }

    /**
     * Функция, описывающая попытку атаки
     * Эта функция объединяет в себе всю структуру атаки: проверка на попадание, расчет урона, нанесение урона.
     *
     * @param player     Данные об игроке
     * @param enemy      Данные об монстре
     * @param battleInfo Данные о бое
     * @param currTurn   Определяет, чья очередь выполнить атаку
     */
    public void attack(Player player, Enemies enemy, CharacterType currTurn, BattleInfoType battleInfo) {
        switch (currTurn) {
            case PLAYER -> {
                if (enemy.getHealth() == 0) {
                    return;
                }
                if (checkHit(player, enemy, PLAYER)) {
                    int newHealth = Math.max(enemy.getHealth() - calculateDamage(player, enemy, PLAYER, battleInfo), 0);
                    // System.out.println("у ENEMIES "  + "Было hp: " + enemy.getHealth() + " Стало: " + newHealth);
                    enemy.setHealth(newHealth);
                }
                if (enemy.getHealth() == 0) {
                    player.setGold(player.getGold() + calculateLoot(enemy));
                    // System.out.println("Голда у игрока: " + player.getGold());
                }
            }
            case ENEMIES -> {
                if (player.getHealth() == 0) {
                    return;
                }
                if (checkHit(player, enemy, ENEMIES)) {
                    int newHealth = Math.max(player.getHealth() - calculateDamage(player, enemy, ENEMIES, battleInfo), 0);
                    // System.out.println("у PLAYER "  + "Было hp: " + player.getHealth() + " Стало: " + newHealth);
                    player.setHealth(newHealth);
                }
            }
        }
    }

    /**
     * @defgroup monster_attack Формулы атаки монстров
     * Модуль описывает функции, описывающие формулы, по которым вычисляется урон монстров
     * Урон зависит от силы (за исключением вампира), также для некоторых из монстров присутствуют уникальные элементы, такие как шанс усыпить игрока у змеи
     */

    /**
     * Функция, высчитывающая, произойдёт ли попадание
     *
     * @param currTurn Определяет, чья очередь выполнить атаку
     * @param enemy    Данные о монстре
     * @param player   Данные об игроке
     */
    public boolean checkHit(Character player, Enemies enemy, CharacterType currTurn) {
        boolean wasHit = false;
        int chance = 0;
        switch (currTurn) {
            case PLAYER -> chance = hitChanceFormula(player.getAgility(), enemy.getAgility());
            case ENEMIES -> chance = hitChanceFormula(enemy.getAgility(), player.getAgility());
        }
        boolean isOgre = enemy.getType() == EnemiesType.OGRE;
        int random = (int) (randomValueDouble() * 100);
        if ((chance > random) || isOgre) {
            wasHit = true;
        }
        // if (wasHit) { System.out.println(currTurn + " Попал по противнику"); } else { System.out.println(currTurn + " Промахнулся по противнику"); }
        return wasHit;
    }

    /**
     * Функция, высчитывающая урон
     *
     * @param player     Данные об игроке
     * @param enemy      Данные о монстре
     * @param battleInfo Данные о бое
     * @param currTurn   Определяет, чья очередь выполнить атаку
     * @return количество урона, наносимого противнику
     */
    public int calculateDamage(Player player, Enemies enemy, CharacterType currTurn, BattleInfoType battleInfo) {
        int damage = 0;
        switch (currTurn) {
            case PLAYER -> {
                if (enemy.getType() == EnemiesType.VAMPIRE && battleInfo.vampireFirstAttack) {
                    battleInfo.vampireFirstAttack = false;
                } else {
                    battleInfo.playerAsSleep = false;
                }
                damage = (int) (player.getBuffStrength() * 0.5);
            }
            case ENEMIES -> {
                if (enemy instanceof Vampire) {
                    damage = vampireDamageFormula(player);
                } else {
                    damage = EnemyDamageFormula(enemy, battleInfo);
                }
            }
        }
        return damage;
    }

    /**
     * Функция вычисления шанса попадания
     * Зависит от ловкости и скорости атакующего и цели
     *
     * @param attackerAgility ловкость атакующего
     * @param targetAgility   ловкость цели
     * @return Шанс попадания
     */
    int hitChanceFormula(int attackerAgility, int targetAgility) {
        if (attackerAgility >= targetAgility) {
            return (int) (((attackerAgility - targetAgility) * 0.3) + 70);
        }
        return (int) (70 - (targetAgility - attackerAgility) * 0.3);
    }

    /**
     * Функция вычисления урона вампира
     * Вампир отнимает 10 процентов от максимального здоровья игрока
     *
     * @param player Информация об игроке
     * @return Количество урона, наносимое монстром игроку
     */
    int vampireDamageFormula(Player player) {
        return player.getMaxHealth() / 10;
    }

    /**
     * Функция, определяющая количество сокровищ, получаемых игроком за убийства противника
     * Количество зависит от сложности противника и небольшого рандома
     *
     * @param enemy данные о монстре
     * @return стоимость сокровища
     */
    public int calculateLoot(Enemies enemy) {
        return (int) ((enemy.getAgility() * 0.4) + (enemy.getStrength() * 0.4) + randomNumber(1, 10));
    }

    /**
     * Функция, удаляющая информацию о монстре из игры
     * Удаление происходит путем стирания информации из массива с сохранением изначального порядка
     * @param room Данные о комнате, к которой принадлежит монстр
     * @param enemy Данные о монстре, который должен быть удален
     */
    //public void deleteEnemyInfo(Room room, Enemies enemy) {
    //
    //}

    /**
     * Функция обновления статуса боёв
     * Сначала функция проверяет монстров на контакт с игроком (check_contact()) и создает структуру боя при помощи init_battle(), если контакт произошел.
     * После функция проверяет уже идущие бои на их завершение(игрок убежал (функция check_contact()), монстр умер) и деинициализирует эти записи
     * @param player Текущее положение игрока в пространстве
     * @param level Данные о начинке уровне
     * @param battlesArray battles_array Массив, содержащий инфу о всех боях
     */
    //public void updateFightStatus(Position player, Level level, BattleInfoType battlesArray) {
    //
    //}

    /**
     * Функция, записывающая информацию о бое в структуру
     * Выбирается первая доступная структура (доступной считается структура с флажком is_fight = false)
     *
     * @param enemy        Информация о противнике игрока
     * @param battlesArray Массив, содержащий инфу о всех боях
     */
    public void initBattle(Enemies enemy, BattleInfoType battlesArray) {

    }

    /**
     * Функция проверки контакта игрока с противником
     * Функция смотрит, чтобы игрок был на расстоянии одной клетки от противника
     *
     * @param player Координаты игрока
     * @param enemy  Данные о противнике
     * @return true, если контакт есть, false в ином случае
     */
    boolean checkContact(Position player, Enemies enemy) {
        return true;
    }


    /**
     * Функция проверки, атаковал ли игрок противника
     * Функция проверяет, походил ли игрок на моба, и если да, то вызывает функцию attack() для игрока
     *
     * @param player         Данные об игроке
     * @param battle         Данные о битве
     * @param playerChoseDir Выбранное игроком направление хода
     * @return true, если игрок попытался совершить атаку, false в ином случае
     */
    boolean checkPlayerAttack(Player player, BattleInfoType battle, DirectionType playerChoseDir) {
        return true;
    }


    /**
     * Функция, очищающая данные о монстрах в листе
     * Функция проходится по комнатам уровня, проверяя хп каждого монстра, если 0, то удаляет данные о нем
     * @param level Информация об уровне
     */
    //public void removeDeadEnemy(Level level) {
    //
    //}


    /**
     * Функция проверки на совпадение координат
     *
     * @param firstPosition  Координаты первого объекта
     * @param secondPosition Координаты второго объекта
     * @return true, если координаты совпали, false в ином случае
     */
    boolean checkEqualCoordinats(Position firstPosition, Position secondPosition) {
        return true;
    }

    /**
     * Функция проверки на соседство координат
     *
     * @param firstPosition  Координаты первого объекта
     * @param secondPosition Координаты второго объекта
     * @return true, если координаты примыкают друг к другу, false в ином случае
     */
    boolean checkIfNeighborTile(Position firstPosition, Position secondPosition) {
        return true;
    }

    /**
     * Функция проверки на существование боя
     * Функция проверяет на совпадения данные монстра, который потенциально может создать новую запись о бое с уже существующими
     *
     * @param enemy        Данные о монстре
     * @param battlesArray Данные о боях
     */
    boolean checkUnique(Enemies enemy, BattleInfoType battlesArray) {
        // +-1/
        return true;
    }
}
