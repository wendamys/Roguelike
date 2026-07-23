package domain.characters.enemies;

import domain.ai.*;
import domain.backpack.ItemsType;
import domain.characters.Enemies;
import domain.gameSession.DifficultyType;
import domain.map.TileType;
import domain.map.Level;
import domain.navigator.Position;

import static domain.MathUtils.MathUtils.randomNumber;

public class Mimic extends Enemies {

    private final EnemiesType type = EnemiesType.MIMIC;
    private int health = 150;
    private int agility = 80;
    private int strength = 10;
    private ItemsType itemsType;

    public Mimic(Position position, DifficultyType difficulty) {
        super(position, difficulty);
        super.setHealthBegin((int) (health * difficulty.getCoef() * Level.getCoefEnemy()));
        super.setMaxHealth(super.getHealth());
        super.setAgilityRand((int) (agility * difficulty.getCoef() * Level.getCoefEnemy()));
        super.setStrengthRand((int) (strength * difficulty.getCoef() * Level.getCoefEnemy()));
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

    /**
     * Возвращает символ отображения мимика в зависимости от состояния
     * @return символ предмета если имитирует, 'm' если атакует
     */
    public char getDisplaySymbol() {
        if (getAmbushAI().isMimicking()) {
            // В режиме имитации возвращаем символ предмета
            return switch (itemsType) {
                case FOOD -> 'F';
                case SCROLL -> 'S';
                case WEAPON -> 'W';
                case ELIXIR -> 'E';
            };
        }
        // В агрессивном режиме возвращаем 'm'
        return 'm';
    }

    /**
     * Возвращает TileType для отображения мимика
     */
    public TileType getTileType() {
        if (getAmbushAI().isMimicking()) {
            // В режиме имитации возвращаем соответствующий TileType предмета
            return switch (itemsType) {
                case FOOD -> TileType.FOOD;
                case SCROLL -> TileType.SCROLL;
                case WEAPON -> TileType.WEAPON;
                case ELIXIR -> TileType.ELIXIR;
            };
        }
        // В агрессивном режиме возвращаем MIMIC
        return TileType.MIMIC;
    }

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
