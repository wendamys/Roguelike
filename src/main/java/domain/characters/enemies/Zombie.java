package domain.characters.enemies;

import domain.ai.AggressiveAI;
import domain.ai.EnemyAI;
import domain.characters.Enemies;
import domain.gameSession.DifficultyType;
import domain.map.Level;
import domain.navigator.Position;


public class Zombie extends Enemies {

    private final EnemiesType type = EnemiesType.ZOMBIE;
    private int health = 50;
    private int agility = 15;
    private int strength = 15;

    public Zombie(Position position, DifficultyType difficulty) {
        super(position, difficulty);
        super.setHealthBegin((int) (health * difficulty.getCoef() * Level.getCoefEnemy()));
        super.setMaxHealth(super.getHealth());
        super.setAgilityRand((int) (agility * difficulty.getCoef() * Level.getCoefEnemy()));
        super.setStrengthRand((int) (strength * difficulty.getCoef() * Level.getCoefEnemy()));
    }

    @Override
    protected EnemyAI createAI() {
        return new AggressiveAI();
    }

    @Override
    public EnemiesType getType() {
        return type;
    }

    @Override
    public String getName() {
        return "z";
    }

    @Override
    public int getHostility() {
        return 3;
    }

    @Override
    public String toString() {
        return String.format("Zombie: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s",
                getName(),
                getHealth(),
                getAgility(),
                getStrength(),
                getType()
        );
    }
}
