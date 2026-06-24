package domain.navigator;

import domain.characters.Character;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Метод {@link #distanceTo(Position)} вычисляет дистанцию между двумя объектами
     *
     * @param other позиция второго объекта
     * @return дистанция до объекта
     */
    public double distanceTo(Position other) {
        if (other == null) {
            throw new NullPointerException("Other position cannot be null");
        }
        int dx = getX() - other.getX();
        int dy = getY() - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position enemy)) return false;
        return x == enemy.getX() && y == enemy.getY();
    }

    @Override
    public int hashCode() {
        return x * 15 + y;
    }

//    @Override
//    public String toString() {
//        return String.format("Distance(%.2f)", ;
//    }

    /**
     * Метод {@link #posDir(DirectionType direction)} создает новую позицию
     * в зависимости от того направления, которое прислали
     *
     * @param direction Направление движения
     * @return Position
     */
    public Position posDir(DirectionType direction) {
        int x = getX();
        int y = getY();
        return switch (direction) {
            case FORWARD -> new Position(x, y + 1);
            case DOWN -> new Position(x, y - 1);
            case LEFT -> new Position(x - 1, y);
            case RIGHT -> new Position(x + 1, y);
        };
    }

    public DirectionType convergence(Character player) {

        //  Создаем врага с текущий позицией
        Position enemyPos = new Position(getX(), getY());
        Position playerPos = new Position(player.getPosition().getX(), player.getPosition().getY());

        System.out.println("Coordinate player: " + player.getPosition().getX() + " " + player.getPosition().getY());
        System.out.println("Coordinate enemy: " + enemyPos.getX() + " " + enemyPos.getY());

        // Перебираем пути, находим минимальный в зависимости от distanceTo
        double min = Double.MAX_VALUE;
        DirectionType dirMove = DirectionType.FORWARD;
        for (DirectionType dT : DirectionType.values()) {
            enemyPos = posDir(dT);
            double findRange = enemyPos.distanceTo(playerPos);
            if (findRange <= min) {
                min = findRange;
                dirMove = dT;
            }
            System.out.println("Range to player :" + findRange);
        }
        return dirMove;
    }

}
