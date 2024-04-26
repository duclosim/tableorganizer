package testutils.factory.dto.h2;

import com.glaitozen.tableorganizer.repository.dto.UserH2Dto;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class UserH2DtoFactory {

    public static final List<UserH2Dto> USER_H2_DTO_LIST = Arrays.asList(UserH2DtoFactory.createUserH2Dto("Matt"), UserH2DtoFactory.createUserH2Dto("Simon"),
            UserH2DtoFactory.createUserH2Dto("Nil"), UserH2DtoFactory.createUserH2Dto("Mei"));

    public static UserH2Dto createUserH2Dto() {
        return createUserH2Dto("set nom");
    }

    public static UserH2Dto createUserH2Dto(String nom) {
        return new UserH2Dto(UUID.randomUUID().toString(), nom, "[[2024,5,14],[2024,5,30]," +
                "[2024,5,13],[2024,5,27],[2024,5,11],[2024,6,11],[2024,5,10],[2024,6,23],[2024,5,22],[2024,6,19]," +
                "[2024,5,18],[2024,5,16]]");
    }

    public static List<UserH2Dto> createUserH2DTOList(int nbDto) {
        return IntStream.range(0, nbDto)
                .mapToObj(k -> createUserH2Dto("user nb " + k))
                .toList();
    }
}
