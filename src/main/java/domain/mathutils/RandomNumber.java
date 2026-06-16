package domain.mathutils;

import java.util.Random;

public class RandomNumber {
    protected static Random random = new Random();

    public int randomNumber(int from, int before) {
        return random.nextInt(from, before + 1);
    }
}
