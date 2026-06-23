package domain.map;

public class Level {
    private int level;
    private double coefItem = 1;
    private double coefEnemy = 1;

    public Level() {}

    public double getCoefItem() {
        return coefItem;
    }
    public void setCoefItem(double coefItem) {
        this.coefItem = coefItem;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public double getCoefEnemy() {
        return coefEnemy;
    }

    public void setCoefEnemy(double coefEnemy) {
        this.coefEnemy = coefEnemy;
    }
}
