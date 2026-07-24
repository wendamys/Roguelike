package domain.map;

public enum ColorKey {
    GREEN, BLUE, RED, YELLOW;

    /**
     * метод возвращает русское название цвета
     */
    public String getRuName() {
        return switch (this) {
            case GREEN -> "зелёный";
            case BLUE -> "синий";
            case RED -> "красный";
            case YELLOW -> "жёлтый";
        };
    }
}
