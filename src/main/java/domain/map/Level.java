package domain.map;

public class Level {
    private int level;
    private double coefItem = 1;


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
}
