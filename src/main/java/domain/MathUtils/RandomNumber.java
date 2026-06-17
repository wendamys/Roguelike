package domain.MathUtils;

import java.util.Random;

public class RandomNumber {
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
}
