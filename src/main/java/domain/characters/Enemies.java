package domain.characters;

import domain.characters.enemies.EnemiesType;
import domain.navigator.DirectionType;
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

    /**
     * метод {@link #isHostility(Player)} выбирает лучшее направление движения до игрока
     * @param player игрок
     * @return направление движения
     */
    public DirectionType convergence(Player player) {
        double min = Double.MAX_VALUE;
        DirectionType dirMove = null;
        for (DirectionType dT : DirectionType.values()) {
            double findRange = getPosition().posDir(dT).distanceTo(player.getPosition());
            if (findRange <= min) {
                min = findRange;
                dirMove = dT;
            }
        }
        return dirMove;
    }

    /**
     * метод {@link #convergenceIsHostility(Player player)} проверяет в радиусе агра ли игрок
     * @param player игрок
     * @return Направление движения, либо null
     */
    public DirectionType convergenceIsHostility(Player player) {
        if (isHostility(player)) return convergence(player);
        return null;
    }
}