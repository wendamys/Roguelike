package domain.MathUtils;

import domain.backpack.ItemsSubType;
import domain.navigator.DirectionType;

import java.util.Random;

public class MathUtils {
    protected static Random random = new Random();

    /**
     * Метод {@link #randomNumber(int from, int before)} рандомно выбирает число от from до before
     *
     * @param from   начало выборки
     * @param before конец выборки
     * @return Ожидаемое число
     */
    public static int randomNumber(int from, int before) {
        return random.nextInt(from, before + 1);
    }

    /**
     * Метод {@link #randomValueDouble()} для генерации рандомного числа с плавающей точкой
     *
     * @return число с плавающей точкой
     */
    public static double randomValueDouble() {return random.nextDouble();}

    public static ItemsSubType randomItemsSubType() {
        return switch (randomNumber(1, 3)) {
            case 1 -> ItemsSubType.HEALTH;
            case 2 -> ItemsSubType.AGILITY;
            case 3 -> ItemsSubType.STRENGTH;
            default -> throw new IllegalStateException("Unexpected value: " + randomNumber(1, 3));
        };
    }

    /**
     * Метод {@link #randomDirection()} выбирает рандомно направление
     *
     * @return Направление
     */
    public static DirectionType randomDirection() {
        return switch (randomNumber(1, 4)) {
            case 1 -> DirectionType.FORWARD;
            case 2 -> DirectionType.DOWN;
            case 3 -> DirectionType.RIGHT;
            default -> DirectionType.LEFT;
        };
    }
}
