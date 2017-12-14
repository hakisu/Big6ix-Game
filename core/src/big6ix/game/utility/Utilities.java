package big6ix.game.utility;

import java.util.Random;

public class Utilities {

    private static Random random;

    static {
        random = new Random();
    }

    public static int generateRandomInt(int min, int max) {
        int randomInt = random.nextInt(max - min + 1) + min;

        return randomInt;
    }
}
