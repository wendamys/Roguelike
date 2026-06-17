package domain.characters;

import domain.characters.enemies.EnemiesType;
import domain.navigator.Position;

abstract public class Enemies extends Character {

    private final EnemiesType type;
    private final int hostility;

    public Enemies(String name, EnemiesType type, int hostility, int health, int agility, int strength, Position position) {
        super(name, health, agility, strength, position);
        this.type = type;
        this.hostility = hostility;
    }

    public int getHostility() {
        return hostility;
    }

    public EnemiesType getType() {
        return type;
    }

    //public boolean convergence(Position positionPlayer, int distance) {
//
    //    //  Создаем врага с текущий позицией
    //    Position enemyMove = new Position(getPosition().getX(), getPosition().getY());
//
    //    System.out.println("Coordinate player: " + positionPlayer.getX() + " " + positionPlayer.getY());
    //    System.out.println("Coordinate enemy: " + enemyMove.getX() + " " + enemyMove.getY());
//
    //    // Перебираем пути, находим минимальный, идем туда, если игрок близко возвращаем true
    //    double min = Double.MAX_VALUE;
    //    DirectionType directionInit = DirectionType.RIGHT;
    //    MovementSystem mvs = new MovementSystem();
    //    for (DirectionType dT : DirectionType.values()) {
    //        Position enemyPos = mvs.moveDir(dT, positionPlayer);
    //        double findRange = enemyPos.distanceTo(positionPlayer);
    //        if (findRange <= min) {
    //            min = findRange;
    //            directionInit = dT;
    //        }
    //        System.out.println("Range to player :" + findRange);
    //    }
    //    if (min != 0.0) {
    //        setPosition(mvs.moveDir(directionInit, positionPlayer));
    //        return false;
    //    } else {
    //        System.out.println("Attack player!");
    //        return true;
    //    }
    //}
}//
