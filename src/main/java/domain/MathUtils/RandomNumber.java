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
    public static int randomNumber(int from, int before) {
        return random.nextInt(from, before + 1);
    }

    /**
     * Метод {@link #randomValueDouble()} для генерации рандомного числа с плавающей точкой
     * @return число с плавающей точкой
     */
    public static double randomValueDouble() {
        return random.nextDouble();
    }
}


