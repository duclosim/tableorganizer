package testutils.factory.model;

import com.glaitozen.tableorganizer.core.model.User;
import testutils.RandomLocalDateFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class UserFactory {
    private UserFactory() {}

    public static final List<User> USER_LIST = Arrays.asList(UserFactory.createUser("Matt"), UserFactory.createUser("Simon"),
            UserFactory.createUser("Nil"), UserFactory.createUser("Mei"));

    private static final int NB_DATES_OCCUPEES = 3;

    public static User createUser() {
        return createUser("default user name");
    }

    public static User createUser(String userName) {
        User user = new User(userName);
        IntStream.range(0, NB_DATES_OCCUPEES).forEach(k -> user.datesOccupees().add(RandomLocalDateFactory.randomFutureLocalDate()));
        return user;
    }

    public static List<User> createUserList(int nbUsers) {
        return IntStream.range(0, nbUsers)
                .mapToObj(k -> createUser("user nb " + k))
                .toList();
    }
}
