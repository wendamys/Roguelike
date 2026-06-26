package domain.characters;

import domain.characters.enemies.EnemiesType;
import domain.navigator.DirectionType;
import domain.navigator.MovementSystem;
import domain.navigator.Position;


abstract public class Enemies extends Character {

    protected EnemiesType type;
    DirectionType dir; // Направление в котором двигался монстр
    private int hostility;
    // (используется для змея, который должен постоянно менять направление движения)

    public Enemies(Position position) {
        super(position);
    }

    public int getHostility() {
        return hostility;
    }

    public void setHostility(int hostility) { this.hostility = hostility;
    }

    public EnemiesType getType() {
        return type;
    }

    /**
     * метод {@link #isHostility(Player)} проверяет, входит ли игрок в радиус агра врага
     * @param player игрок
     * @return входит/не входит
     */
    public boolean isHostility(Player player) {
        return getPosition().distanceTo(player.getPosition()) <= getHostility();
    }

    public DirectionType convergence(Player player) {

        // System.out.println("Coordinate player: " + player.getPosition().getX() + " " + player.getPosition().getY());
        // System.out.println("Coordinate enemy: " + getPosition().getX() + " " + getPosition().getY());
        // Перебираем пути, находим минимальный в зависимости от distanceTo
        double min = Double.MAX_VALUE;
        DirectionType dirMove = null;
        for (DirectionType dT : DirectionType.values()) {
            double findRange = getPosition().posDir(dT).distanceTo(player.getPosition());
            if (findRange <= min) {
                min = findRange;
                dirMove = dT;
            }
            // System.out.println("Range to player :" + findRange);
        }
        return dirMove;
    }

    public DirectionType convergenceIsHostility(Player player) {
        if (isHostility(player)) return convergence(player);
        return null;
    }


}