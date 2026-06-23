package domain.MathUtils;

import domain.backpack.ItemsSubType;

import java.util.Random;

public class MathUtils {
    protected static Random random = new Random();

    /**
     * Метод {@link #randomNumber(int from, int before)} рандомно выбирает число от from до before
     * @param from начало выборки
     * @param before конец выборки
     * @return Ожидаемое число
     */
    public static int randomNumber(int from, int before) {
        return random.nextInt(from, before + 1);
    }

    /**
     * Метод {@link #randomValueDouble()} для генерации рандомного числа с плавающей точкой
     * @return число с плавающей точкой
     */
    public static double randomValueDouble() { return random.nextDouble(); }

    public static ItemsSubType randomType() {
        return switch (randomNumber(1, 3)) {
            case 1 -> ItemsSubType.HEALTH;
            case 2 -> ItemsSubType.AGILITY;
            case 3 -> ItemsSubType.STRENGTH;
            default -> throw new IllegalStateException("Unexpected value: " + randomNumber(1, 3));
        };
    }
}
