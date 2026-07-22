package domain.characters.enemies;

import domain.ai.*;
import domain.backpack.ItemsType;
import domain.characters.Enemies;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;

public class Mimic extends Enemies {

    private final EnemiesType type = EnemiesType.MIMIC;
    private int health = 150;
    private int agility = 80;
    private int strength = 10;
    private ItemsType itemsType;

    public Mimic(Position position) {
        super(position);
        super.setHealthBegin((int) (health * Level.getCoefEnemy()));
        super.setMaxHealth(super.getHealth());
        super.setAgilityRand((int) (agility * Level.getCoefEnemy()));
        super.setStrengthRand((int) (strength * Level.getCoefEnemy()));
        itemsType = randomItem();
    }

    @Override
    protected EnemyAI createAI() {
        return new AmbushAI();
    }
    
    public AmbushAI getAmbushAI() {
        return (AmbushAI) ai;
    }

    @Override
    public EnemiesType getType() {
        return type;
    }

    @Override
    public String getName() {
        return "m";
    }

    @Override
    public int getHostility() {
        return 1;
    }

    public ItemsType getItemsType() {return itemsType;}

    private ItemsType randomItem() {
        switch (randomNumber(1, 10)) {
            case 1 -> { return ItemsType.ELIXIR; }
            case 2, 3, 4 -> { return ItemsType.SCROLL; }
            case 10 -> { return  ItemsType.WEAPON; }
            default -> { return  ItemsType.FOOD; }
        }
    }

    @Override
    public String toString() {
        return String.format("Mimiс: \nname: %s\nhealth: %d\nagility: %d\nstrength: %d\ntype: %s", getName(), getHealth(), getAgility(), getStrength(), getType());
    }
}
