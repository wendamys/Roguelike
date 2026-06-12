package domain.MathUtils;

import java.util.Random;

public class RandomNumber {
    protected static Random random = new Random();

    public int randomNumber(int from, int before) {
        int randomNumber = random.nextInt(from, before + 1);
        return randomNumber;
    }
}
