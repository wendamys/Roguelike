package domain.navigator;

import domain.characters.Character;
import domain.characters.Enemies;
import domain.characters.Player;
import domain.characters.enemies.Ogre;
import domain.map.Room;

import static domain.MathUtils.MathUtils.randomDirection;

public class MovementSystem {

    /**
     * Метод {@link #moveDir(DirectionType direction, Character characte)} ходит по заданному направлению
     *
     * @param direction Направление движения
     * @param character Меняет текущую позицию переданному объекту
     */
    public void moveDir(DirectionType direction, Character character) {
        int x = character.getPosition().getX();
        int y = character.getPosition().getY();
        switch (direction) {
            case FORWARD -> character.setPosition(new Position(x, y + 1));
            case DOWN -> character.setPosition(new Position(x, y - 1));
            case LEFT -> character.setPosition(new Position(x - 1, y));
            case RIGHT -> character.setPosition(new Position(x + 1, y));
        }
    }

    /**
     * Метод {@link #moveRandom} выбирает рандомно направление движения
     * и вызывает метод moveDir, который ходит по заданному направлению
     */
    public void moveRandom(Character character) {
        moveDir(randomDirection(), character);
    }

    /**
     * Метод {@link #EnemyGameMove(Player player, Enemies enemy)} служит для того
     * чтобы моб ходил рандомно до того момента пока не игрок не попадет в его радиус
     *
     * @param player игрок
     * @param enemy противник
     */
    public void EnemyGameMove(Player player, Enemies enemy) {
        while (enemy.getHealth() > 0) {
            if (enemy.convergenceIsHostility(player) == null) moveRandom(enemy);
            else moveDir(enemy.convergenceIsHostility(player), enemy);
        }
    }

    /**
     * метод {@link #checkPosition(Character, Character)} проверяет занята ли позиция другим персонажем
     * @param character1 персонаж 1
     * @param character2 персонаж 2
     * @return true - занята, false - открыта
     */
    public boolean checkPosition(Character character1, Character character2) {
        return character1.getPosition().equals(character2.getPosition());
    }

    /**
     * метод {@link #characterOutsideBorder(Character, Room)} проверяет,
     * находится ли персонаж вне пределов комнаты
     * @param character персонаж
     * @param room комната
     * @return true - пресонаж вне пределов, false - внутри комнаты
     */
    public boolean characterOutsideBorder(Character character, Room room) {
        double charX = character.getPosition().getX();
        double charY = character.getPosition().getY();

        double roomLeft = room.getPosition().getX() + 1;
        double roomRight = room.getPosition().getX() + room.getWidth() - 1;
        double roomTop = room.getPosition().getY() + 1;
        double roomBottom = room.getPosition().getY() + room.getHeight() - 1;

        boolean outsideLeft = charX < roomLeft;
        boolean outsideRight = charX > roomRight;
        boolean outsideTop = charY < roomTop;
        boolean outsideBottom = charY > roomBottom;

        return outsideLeft || outsideRight || outsideTop || outsideBottom;
    }

    /**
     * @brief Функция, проверяющая, находится игрок вне границ или нет
     *
     * Для каждой из комнат и коридоров проверяется, находится ли игрок внутри,
     * до момента пока не найдется совпадение или не закончатся объекты
     * @param[in] level Описание уровня, где содержится вся информация о комнатах и коридорах
     * @param[in] player_coordinates Информация о положении игрока в пространстве
     * @return true, если игрок вне границ, в ином случае false
     */
//    bool check_outside_border(const object_t *player_coordinates, const level_t *level)
//    {
//        bool outside_border = true;
//        for (size_t i = 0; i < ROOMS_NUM && outside_border; i++)
//            outside_border = character_outside_border(player_coordinates, &level->rooms[i].coords);
//        for (size_t i = 0; i < level->passages.passages_num && outside_border; i++)
//            outside_border = character_outside_border(player_coordinates, &level->passages.passages[i]);
//        return outside_border;
//    }
}