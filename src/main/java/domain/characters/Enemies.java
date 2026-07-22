package domain.characters;

import domain.ai.*;
import domain.characters.enemies.EnemiesType;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import java.util.function.Predicate;

import static domain.MathUtils.MathUtils.randomNumber;


abstract public class Enemies extends Character {

    protected EnemiesType type;
    private String name;
    private int health;
    private int maxHealth;
    private int agility;
    private int strength;
    private int hostility;


    protected boolean isInvisible = false;
    protected boolean isStunned = false;
    protected boolean isMimicking = false;

    protected EnemyAI ai;

    public Enemies(Position position) {
        super(position);
        this.ai = createAI();
    }

    public EnemiesType getType() {return type;}
    public void setType(EnemiesType type) {this.type = type;}

    @Override
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    @Override
    public int getHealth() {return health;}
    @Override
    public void setHealth(int health) {this.health = health;}

    public int getMaxHealth() {return maxHealth;}
    public void setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}

    @Override
    public int getAgility() {return this.agility;}
    public void setAgility(int agility) {this.agility = agility;}

    @Override
    public int getStrength() {return strength;}
    public void setStrength(int strength) {this.strength = strength;}

    public int getHostility() {return hostility;}
    public void setHostility(int hostility) { this.hostility = hostility; }

    public boolean getIsInvisible() { return isInvisible; }
    public void setIsInvisible(boolean invisible) { isInvisible = invisible; }

    public boolean getIsStunned() { return isStunned; }
    public void setIsStunned(boolean stunned) { isStunned = stunned; }

    public boolean getIsMimicking() { return isMimicking; }
    public void setIsMimicking(boolean mimicking) { isMimicking = mimicking; }

    // setters randomly
    public void setAgilityRand(int agility) {
        this.agility = randomNumber((int) (agility * 0.9), (int) (agility * 1.1));
    }
    public void setHealthBegin(int health) {
        this.health = randomNumber((int) (health * 0.9), (int) (health * 1.1));
    }
    public void setStrengthRand(int strength) {
        this.strength = randomNumber((int) (strength * 0.9), (int) (strength * 1.1));
    }

    /**
     * Метод создает AI для конкретного типа врага
     * @return экземпляр AI
     */
    protected EnemyAI createAI() {
        return new AggressiveAI();
    }

    /**
     * метод проверяет, входит ли игрок в радиус агра врага
     * Враги атакуют только если находятся на соседней клетке (чебышевское расстояние <= 1)
     * @param player игрок
     * @return входит/не входит
     */
    public boolean isHostility(Player player) {
        Position enemyPos = getPosition();
        Position playerPos = player.getPosition();
        int dx = Math.abs(enemyPos.getX() - playerPos.getX());
        int dy = Math.abs(enemyPos.getY() - playerPos.getY());
        return dx <= getHostility() && dy <= getHostility();
    }

    /**
     * метод выбирает лучшее направление движения до игрока среди проходимых клеток
     * @param player игрок
     * @param walkable проверка проходимости клетки
     * @return направление движения или null, если идти некуда
     */
    public DirectionType convergence(Player player, Predicate<Position> walkable) {
        double min = Double.MAX_VALUE;
        DirectionType dirMove = null;
        for (DirectionType dT : DirectionType.values()) {
            Position next = getPosition().posDir(dT);
            if (!walkable.test(next)) {
                continue;
            }
            double findRange = next.distanceTo(player.getPosition());
            if (findRange < min) {
                min = findRange;
                dirMove = dT;
            }
        }
        return dirMove;
    }

    /**
     * метод проверяет в радиусе агра ли игрок
     * @param player игрок
     * @param walkable проверка проходимости клетки
     * @return Направление движения, либо null
     */
    public DirectionType convergenceIsHostility(Player player, Predicate<Position> walkable) {
        if (isHostility(player)) return convergence(player, walkable);
        return null;
    }

    /**
     * метод выбирает направление движения на основе AI
     * @param player игрок
     * @param walkable проверка проходимости клетки
     * @return направление движения или null
     */
    public DirectionType decideMove(Player player, Predicate<Position> walkable) {
        if (isStunned) {
            isStunned = false;
            return null;
        }
        return ai.decideMove(this, player, walkable);
    }
}
