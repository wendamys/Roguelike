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
    public int randomNumber(int from, int before) {
        return random.nextInt(from, before + 1);
    }

    public ItemsSubType randomType() {
        MathUtils randomNumber = new MathUtils();
        ItemsSubType subType = ItemsSubType.HEALTH;
        return switch (randomNumber.randomNumber(1, 3)) {
            case 1 -> subType = ItemsSubType.HEALTH;
            case 2 -> subType = ItemsSubType.AGILITY;
            case 3 -> subType = ItemsSubType.STRENGTH;
            default -> throw new IllegalStateException("Unexpected value: " + randomNumber.randomNumber(1, 3));
        };
    }
}
