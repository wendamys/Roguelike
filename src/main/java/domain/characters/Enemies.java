package domain.characters;

import domain.characters.enemies.EnemiesType;
import domain.navigator.DirectionType;
import domain.navigator.Position;
import domain.MathUtils.RandomNumber;

abstract public class Enemies extends Character {

    private final EnemiesType type;
    private final int hostility;

    public Enemies(String name, EnemiesType type, int hostility, int health, int agility, int strength, Position positionInter) {
        super(name, health, agility, strength, positionInter);
        this.type = type;
        this.hostility = hostility;
    }

    public int getHostility() {
        return hostility;
    }

    public EnemiesType getType() {
        return type;
    }

    public DirectionType randomDirection() {
        RandomNumber randomDir = new RandomNumber();
        return switch (randomDir.randomNumber(1, 4)) {
            case 1 -> DirectionType.FORWARD;
            case 2 -> DirectionType.DOWN;
            case 3 -> DirectionType.RIGHT;
            case 4 -> DirectionType.LEFT;
            default -> throw new IllegalArgumentException("Error num randomDirection");
        };
    }

    public void moveRandom(int distance) {
        Position currentPos = getPosition();
        Position pos = new Position(currentPos.getX(), currentPos.getY());
        Position newPos = pos.moveDir(randomDirection(), 1);
        setPosition(newPos);
    }

    public void convergence(Position positionPlayer, int distance) {

        // Определяем позицию врага
        Position currentPosEnemies = getPosition();

        //  Создали нового врага с позицией текущего врага
        Position enemyMove = new Position(currentPosEnemies.getX(), currentPosEnemies.getY());

        // 0, 10
        System.out.println("Coordinate player: " + positionPlayer.getX() + positionPlayer.getY());
        // 0, 0
        System.out.println("Coordinate enemy: " + enemyMove.getX() + enemyMove.getY());

        // Нужно просто указать переменные т.к. чтобы было видно и внутри фора и снаружи
        double min = Double.MAX_VALUE;
        DirectionType dirMove = DirectionType.RIGHT;
        // Тут пробегаемся по типам, которые лежат в DirectionType
        for (DirectionType dT: DirectionType.values()) {
            // Мы ходим в одном направлении, которое лежит в типах
            Position enemyPos = enemyMove.moveDir(dT, 1);
            // Сразу высчитываю расстояние от противника до игрока
            double findRange = enemyPos.distanceTo(positionPlayer);
            // Проверка на минимальное значение и запись минимального значения
            // для следующей сверки и запись куда нужно идти
            if (findRange <= min) {
                min = findRange;
                dirMove = dT;
            }
            System.out.println("Range to player :" + findRange);
        }
        setPosition(enemyMove.moveDir(dirMove, 1));


        //ImmutablePosition enemyMoveF = enemyPos.moveDir(DirectionType.FORWARD, 1);
        //ImmutablePosition enemyMoveD = enemyPos.moveDir(DirectionType.DOWN, 1);
        //ImmutablePosition enemyMoveL = enemyPos.moveDir(DirectionType.LEFT, 1);
        //ImmutablePosition enemyMoveR = enemyPos.moveDir(DirectionType.RIGHT, 1);
//
//
//
        ////Создаем объекты, которые сходили в разные стороны
        //ImmutableDistance enemyDisF = new ImmutableDistance(enemyMoveF.getX(), enemyMoveF.getY());
        //ImmutableDistance enemyDisD = new ImmutableDistance(enemyMoveD.getX(), enemyMoveD.getY());
        //ImmutableDistance enemyDisL = new ImmutableDistance(enemyMoveL.getX(), enemyMoveL.getY());
        //ImmutableDistance enemyDisR = new ImmutableDistance(enemyMoveR.getX(), enemyMoveR.getY());


        //Тоже самое что и ниже, только для одного пока(дальше должен будет быть список, (это я дописал сейчас, декомпозиция)
        //double check = findRangeDistanceToPlayer(positionPlayer, enemyDisF);
//
        ////Вычисляем дистанцию по формуле радиуса для всех враждебных позиций
        //double rangeF = enemyDisF.distanceTo(positionPlayer);
        //double rangeD = enemyDisD.distanceTo(positionPlayer);
        //double rangeL = enemyDisL.distanceTo(positionPlayer);
        //double rangeR = enemyDisR.distanceTo(positionPlayer);
//
//
        //System.out.println("Range to player F: " + rangeF);
        //System.out.println("Range to player D: " + rangeD);
        //System.out.println("Range to player L: " + rangeL);
        //System.out.println("Range to player R: " + rangeR);
//
        //distanceToList.add(rangeF);
        //distanceToList.add(rangeD);
        //distanceToList.add(rangeL);
        //distanceToList.add(rangeR);
//
        //Collections.sort(distanceToList);


        ///Свитч выбирает наименьший элемент т.к. Он отсортирован и сам выбирает направление и позиция врага изменяется в наименьшую сторону
        //switch (distanceToList.getFirst()) {
        //    case Object o when o.equals(rangeF) -> setPosition(enemyPos.moveDir(DirectionType.FORWARD, 1));
        //    case Object o when o.equals(rangeD) -> setPosition(enemyPos.moveDir(DirectionType.DOWN, 1));
        //    case Object o when o.equals(rangeL) -> setPosition(enemyPos.moveDir(DirectionType.LEFT, 1));
        //    case Object o when o.equals(rangeR) -> setPosition(enemyPos.moveDir(DirectionType.RIGHT, 1));
        //    default -> {}
        //}

        //System.out.println(getPosition());

    }
}
