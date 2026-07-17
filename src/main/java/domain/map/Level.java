package domain.map;

public class Level {
    private static int levelUp = 1;

    public Level() {}

    public static int getLevelUp() {
        return levelUp;
    }
    public static void setLevelUp(int levelUp) {
        Level.levelUp = levelUp;
    }

    public static double getCoefEnemy() {
        return (levelUp * 0.1) + 1;
    }
    public static  double getCoefItem() {
        return (levelUp * 0.15) + 1;
    }

    @Override
    public String toString() {
        return String.format(
                "\nLevel:\nlevelUp: %d\ncoefEnemy: %.3f\ncoefItem: %.3f",
                levelUp,
                getCoefEnemy(),
                getCoefItem()
        );
    }

}