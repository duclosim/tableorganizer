package testutils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomNumber {
    private RandomNumber() {}

    /*
     * Retourne un entier aléatoire r tel que r ∈ [min : max]
     */
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
