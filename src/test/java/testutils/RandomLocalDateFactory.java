package testutils;

import java.time.LocalDate;

import static testutils.RandomNumber.randomInt;

public class RandomLocalDateFactory {

    private static final int MIN_PLUS_DAYS = 0;
    public static final int MAX_PLUS_DAYS = 60;

    public static LocalDate randomFutureLocalDate() {
        int randomNum = randomInt(MIN_PLUS_DAYS, MAX_PLUS_DAYS);
        return LocalDate.now().plusDays(randomNum);
    }
}
