package domain.battle;

import domain.characters.Character;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import java.util.ArrayList;

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
     * Функция, высчитывающая, произойдёт ли попадание
     * @param currTurn Определяет, чья очередь выполнить атаку
     * @param enemy Данные о монстре
     * @param  player Данные об игроке
     * @return true, если атакующий попал по цели, иначе false
     */
    public boolean checkHit(AttackSystem currTurn, Character enemy, Player player) {
        return true;
    }

    /**
     * Функция, высчитывающая урон
     * @param player Данные об игроке
     * @param battleInfo Данные о бое
     * @param currTurn Определяет, чья очередь выполнить атаку
     * @return количество урона, наносимого противнику
     */
    public int calculateDamage(Player player, BattleInfoType battleInfo, CharacterType currTurn){
        return 0;
    }

    /**
     * Функция, определяющая количество сокровищ, получаемых игроком за убийства противника
     * Количество зависит от сложности противника
     * @param[in] enemy данные о монстре
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
    public void deleteEnemyInfo(Room room, Enemies enemy) {

    }

    /**
     * Функция обновления статуса боёв
     * Сначала функция проверяет монстров на контакт с игроком (check_contact()) и создает структуру боя при помощи init_battle(), если контакт произошел.
     * После функция проверяет уже идущие бои на их завершение(игрок убежал (функция check_contact()), монстр умер) и деинициализирует эти записи
     * @param player Текущее положение игрока в пространстве
     * @param level Данные о начинке уровне
     * @param battlesArray battles_array Массив, содержащий инфу о всех боях
     */
    public void updateFightStatus(Position player, Level level, BattleInfoType battlesArray) {

    }

    /**
     * Функция, записывающая информацию о бое в структуру
     * Выбирается первая доступная структура (доступной считается структура с флажком is_fight = false)
     * @param enemy Информация о противнике игрока
     * @param battlesArray Массив, содержащий инфу о всех боях
     */
    public void initBattle(Enemies enemy, BattleInfoType battlesArray) {

    }

    /**
     * Функция проверки контакта игрока с противником
     * Функция смотрит, чтобы игрок был на расстоянии одной клетки от противника
     * @param[in] player_coordinates Координаты игрока
     * @param[in] monster Данные о противнике
     * @return true, если контакт есть, false в ином случае
     */
    boolean checkContact(Position player, Enemies enemy) {
        return true;
    }


    /**
     * Функция проверки, атаковал ли игрок противника
     * Функция проверяет, походил ли игрок на моба, и если да, то вызывает функцию attack() для игрока
     * @param player Данные об игроке
     * @param battle Данные о битве
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
    public void removeDeadEnemy(Level level) {

    }


    /**
     * Функция проверки на совпадение координат
     * @param firstPosition Координаты первого объекта
     * @param secondPosition Координаты второго объекта
     * @return true, если координаты совпали, false в ином случае
     */
    boolean checkEqualCoordinats(Position firstPosition, Position secondPosition) {
        return true;
    }

    /**
     * Функция проверки на соседство координат
     * @param firstPosition Координаты первого объекта
     * @param secondPosition Координаты второго объекта
     * @return true, если координаты примыкают друг к другу, false в ином случае
     */
    boolean checkIfNeighborTile(Position firstPosition, Position secondPosition) {
        return true;
    }

    /**
     * Функция проверки на диагональное соседство координат
     * @param firstPosition Координаты первого объекта
     * @param secondPosition Координаты второго объекта
     * @return true, если координаты соединены по диагонали, false в ином случае
     */
    boolean checkIfDiagonallyNeighbourTile(Position firstPosition, Position secondPosition) {
        return true;
    }


    /**
     * Функция проверки на существование боя
     * Функция проверяет на совпадения данные монстра, который потенциально может создать новую запись о бое с уже существующими
     * @param enemy Данные о монстре
     * @param battlesArray Данные о боях
     */
    boolean checkUnique(Enemies enemy, BattleInfoType battlesArray) {
        return true;
    }


    /**
     * Функция вычисления шанса попадания
     * Зависит от ловкости и скорости атакующего и цели
     * @param attackerAgility ловкость атакующего
     * @param targetAgility ловкость цели
     * @return Шанс попадания
     */
    int hitChanceFormula(int attackerAgility, int targetAgility) {
        return 0;
    }

    /**
     * @defgroup monster_attack Формулы атаки монстров
     * Модуль описывает функции, описывающие формулы, по которым вычисляется урон монстров
     * Урон зависит от силы (за исключением вампира), также для некоторых из монстров присутствуют уникальные элементы, такие как шанс усыпить игрока у змеи
     */

    /**
     * Функция вычисления урона вампира
     * Вампир отнимает значение MAX_PART_HP от максимального здоровья игрока
     * @param player Информация об игроке
     * @return Количество урона, наносимое монстром игроку
     */
    int vampireDamageFormula(Player player) {
        return 0;
    }

    /**
     * Функция вычисления урона зомби и призрака
     * @param battleInfo Информация о бое
     * @return Количество урона, наносимое монстром игроку
     */
    int zombieGhostDamageFormula(BattleInfoType battleInfo) {
        return 0;
    }

    /**
     * Функция вычисления урона огра
     * Огр атакует раз в два хода, при этом гарантированно попадая в игрока
     * @param battleInfo Информация о бое
     * @return Количество урона, наносимое монстром игроку
     */
    int ogreDamageFormula(BattleInfoType battleInfo) {
        return 0;
    }


    /**
     *  Функция вычисления урона змеи
     * Змея с шансом SLEEP_CHANCE процентов может усыпить игрока на один ход
     * @param battleInfo Информация о бое
     * @return Количество урона, наносимое монстром игроку
     */
    int snakeDamageFormula(BattleInfoType battleInfo) {
        return 0;
    }

    /// Функция получения координат монстра
    /// @param enemy Информация о монстре
    /// @return координаты, в которых находится монстр
    ArrayList<Integer> getEnemyPosition(Enemies enemy) {
        ArrayList<Integer> listPositionEnemy = new ArrayList<>();
        listPositionEnemy.add(enemy.getPosition().getX());
        listPositionEnemy.add(enemy.getPosition().getY());
        return listPositionEnemy;
    }















}
