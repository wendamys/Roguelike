package domain.characters;

import domain.characters.enemies.EnemiesType;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;


abstract public class Enemies extends Character {


    private int health;
    private int agility;
    private int strength;
    private int hostility;

    protected EnemiesType type;
    DirectionType dir;

    @Override
    public int getHealth() {return health;}

    @Override
    public int getAgility() {return this.agility;}

    @Override
    public int getStrength() {return strength;}
    public int getHostility() {return hostility;}
    public EnemiesType getType() {return type;}

    @Override
    public void setHealth(int health) {this.health = health;}

    public void setAgility(int agility) {
        this.agility = randomNumber((int) (agility * 0.9), (int) (agility * 1.1));
    }

    public void setHealthBegin(int health) {
        this.health = randomNumber((int) (health * 0.9), (int) (health * 1.1));
    }

    public void setStrength(int strength) {
        this.strength = randomNumber((int) (strength * 0.9), (int) (strength * 1.1));
    }



    public Enemies(Position position) {
        super(position);
    }

    /**
     * метод {@link #isHostility(Player)} проверяет, входит ли игрок в радиус агра врага
     *
     * @param player игрок
     * @return входит/не входит
     */
    public boolean isHostility(Player player) {
        return getPosition().distanceTo(player.getPosition()) <= getHostility();
    }

    /**
     * метод {@link #isHostility(Player)} выбирает лучшее направление движения до игрока
     *
     * @param player игрок
     * @return направление движения
     */
    public DirectionType convergence(Player player) {
        double min = Double.MAX_VALUE; DirectionType dirMove = null; for (DirectionType dT : DirectionType.values()) {
            double findRange = getPosition().posDir(dT).distanceTo(player.getPosition()); if (findRange <= min) {
                min = findRange; dirMove = dT;
            }
        } return dirMove;
    }

    /**
     * метод {@link #convergenceIsHostility(Player player)} проверяет в радиусе агра ли игрок
     *
     * @param player игрок
     * @return Направление движения, либо null
     */
    public DirectionType convergenceIsHostility(Player player) {
        if (isHostility(player)) return convergence(player); return null;
    }


}