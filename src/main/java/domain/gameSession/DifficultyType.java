package domain.gameSession;

/**
 * Уровень сложности игры.
 * Коэффициент влияет на статы врагов при спавне и на итоговый счёт,
 * туман войны и ассортимент магазина зависят от самого уровня сложности.
 */
public enum DifficultyType {
    EASY(0.9, 2, 4, "Easy"),
    HARD(1.0, 1, 3, "Hard"),
    VERY_HARD(1.1, 0, 2, "NIGHTMARE");

    private final double coef;
    private final int shopMin;
    private final int shopMax;
    private final String label;

    DifficultyType(double coef, int shopMin, int shopMax, String label) {
        this.coef = coef;
        this.shopMin = shopMin;
        this.shopMax = shopMax;
        this.label = label;
    }

    public double getCoef() { return coef; }

    public int getShopMin() { return shopMin; }

    public int getShopMax() { return shopMax; }

    public String getLabel() { return label; }
}