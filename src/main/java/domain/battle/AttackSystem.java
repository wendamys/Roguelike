package domain.battle;

import domain.characters.Character;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.EnemiesType;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import java.util.ArrayList;

import static domain.MathUtils.RandomNumber.randomValueDouble;
import static domain.battle.CharacterType.*;

public class AttackSystem {

    /**
     * Метод {@link #acceptDamage(int damage, Character character)} описывает получение урона персонажем.
     * Если урона больше чем здоровья, то устанавливает здоровье 0
     *
     * @param damage очки урона
     */
    public void acceptDamage(int damage, Character character) {
        int health = character.getHealth();
        if (damage < 0 || health <= 0) return;
        int newHealth = health - damage;
        health = Math.max(newHealth, 0);
        character.setHealth(health);
    }

    /**
     * Функция, описывающая попытку атаки
     * Эта функция объединяет в себе всю структуру атаки: проверка на попадание, расчет урона, нанесение урона.
     * @param player Данные об игроке
     * @param battleInfo Данные о бое
     * @param currTurn Определяет, чья очередь выполнить атаку
     */
    public void attack(Player player, BattleInfoType battleInfo, CharacterType currTurn) {
        switch (currTurn) {
            case PLAYER -> {
                if (checkHit(player, battleInfo.enemy, PLAYER)) {
                    int health = battleInfo.enemy.getHealth();
                    health -= calculateDamage(player, battleInfo, currTurn);
                    battleInfo.enemy.setHealth(health);
                }
                if (battleInfo.enemy.getHealth() <= 0) {
                    player.setGold(calculateLoot(battleInfo.enemy));
                }
            }
            case ENEMIES -> {
                if (checkHit(player, battleInfo.enemy, ENEMIES)) {
                    int health = player.getHealth();
                    health -= calculateDamage(player, battleInfo, ENEMIES);
                    player.setHealth(health);
                }
            }
        }
    }

    /**
     * Функция, высчитывающая, произойдёт ли попадание
     * @param currTurn Определяет, чья очередь выполнить атаку
     * @param enemy    Данные о монстре
     * @param player   Данные об игроке
     */
    public boolean checkHit(Character player, Enemies enemy, CharacterType currTurn) {
        boolean wasHit = false;
        int chance = 0;
        switch (currTurn) {
            case PLAYER -> chance += hitChanceFormula(player.getAgility(), enemy.getAgility());
            case ENEMIES -> chance += hitChanceFormula(enemy.getAgility(), player.getAgility());
        }
        boolean isOgre = enemy.getSubType() == EnemiesType.OGRE;
        int random = (int) (randomValueDouble() * 100);
        System.out.println("Chance: " + currTurn + ": " + chance);
        System.out.println("Random: " + random);
        if ((chance > random) || isOgre) { wasHit = true; }
        //if (wasHit) {
        //    System.out.println(currTurn + " Попал по противнику");
        //} else {
        //    System.out.println(currTurn + " Промахнулся по противнику");
        //}
        return wasHit;
    }

    /**
     * Функция, высчитывающая урон
     *
     * @param player     Данные об игроке
     * @param battleInfo Данные о бое
     * @param currTurn   Определяет, чья очередь выполнить атаку
     * @return количество урона, наносимого противнику
     */
    public int calculateDamage(Player player, BattleInfoType battleInfo, CharacterType currTurn) {
        int damage = 0;
        int ZOMBIE = zombieGhostDamageFormula(battleInfo);
        boolean VAMPIRE = false;
        int GHOST = 0;
        return 0;
    }

    /**
     * Функция, определяющая количество сокровищ, получаемых игроком за убийства противника
     * Количество зависит от сложности противника
     *
     * @param enemy данные о монстре
     * @return стоимость сокровища
     */
    public int calculateLoot(Enemies enemy) {
        return 0;
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
     * @return true, если контакт есть, false в ином случае
     * @param player Координаты игрока
     * @param enemy Данные о противнике
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
     * Функция, очищающая данные о монстрах
     * Функция проходится по комнатам уровня, проверяя хп каждого монстра, если оно неположительно, то удаляет данные о нем
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
     * Функция проверки на диагональное соседство координат
     *
     * @param firstPosition  Координаты первого объекта
     * @param secondPosition Координаты второго объекта
     * @return true, если координаты соединены по диагонали, false в ином случае
     */
    boolean checkIfDiagonallyNeighbourTile(Position firstPosition, Position secondPosition) {
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
        return true;
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
     * @defgroup monster_attack Формулы атаки монстров
     * Модуль описывает функции, описывающие формулы, по которым вычисляется урон монстров
     * Урон зависит от силы (за исключением вампира), также для некоторых из монстров присутствуют уникальные элементы, такие как шанс усыпить игрока у змеи
     */

    /**
     * Функция вычисления урона вампира
     * Вампир отнимает значение MAX_PART_HP от максимального здоровья игрока
     *
     * @param player Информация об игроке
     * @return Количество урона, наносимое монстром игроку
     */
    int vampireDamageFormula(Player player) {
        return 0;
    }

    /**
     * Функция вычисления урона зомби и призрака
     *
     * @param battleInfo Информация о бое
     * @return Количество урона, наносимое монстром игроку
     */
    int zombieGhostDamageFormula(BattleInfoType battleInfo) {
        return 0;
    }

    /**
     * Функция вычисления урона огра
     * Огр атакует раз в два хода, при этом гарантированно попадая в игрока
     *
     * @param battleInfo Информация о бое
     * @return Количество урона, наносимое монстром игроку
     */
    int ogreDamageFormula(BattleInfoType battleInfo) {
        return 0;
    }


    /**
     * Функция вычисления урона змеи
     * Змея с шансом SLEEP_CHANCE процентов может усыпить игрока на один ход
     *
     * @param battleInfo Информация о бое
     * @return Количество урона, наносимое монстром игроку
     */
    int snakeDamageFormula(BattleInfoType battleInfo) {
        return 0;
    }

    /// Функция получения координат монстра
    ///
    /// @param enemy Информация о монстре
    /// @return координаты, в которых находится монстр
    ArrayList<Integer> getEnemyPosition(Enemies enemy) {
        ArrayList<Integer> listPositionEnemy = new ArrayList<>();
        listPositionEnemy.add(enemy.getPosition().getX());
        listPositionEnemy.add(enemy.getPosition().getY());
        return listPositionEnemy;
    }


}
