package com.glaitozen.tableorganizer.core.mapper.h2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glaitozen.tableorganizer.core.model.User;
import com.glaitozen.tableorganizer.repository.dto.UserH2Dto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testutils.factory.dto.h2.UserH2DtoFactory;
import testutils.factory.model.UserFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class UserH2MapperTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final UserH2Mapper mapper = new UserH2Mapper(objectMapper);

    @BeforeAll
    static void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test
    void mapToDto_with_a_null_model_should_return_null() {
        // WHEN
        UserH2Dto result = mapper.mapToDto(null);
        // THEN
        assertThat(result).isNull();
    }

    @Test
    void mapToDto_with_a_not_null_model_should_map_fields_correctly() {
        // GIVEN
        User user = UserFactory.createUser("Cosmic Girl");
        // WHEN
        UserH2Dto result = mapper.mapToDto(user);
        // THEN
        assertMapUserToDtoIsOk(result, user, objectMapper);
    }

    static void assertMapUserToDtoIsOk(UserH2Dto result, User user, ObjectMapper objectMapper) {
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.id());
        assertThat(result.getNom()).isEqualTo(user.nom());
        try {
            assertThat(result.getDatesOccupees()).isEqualTo(objectMapper.writeValueAsString(user.datesOccupees()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void mapToDtoList_should_map_all_objects_correctly() {
        // GIVEN
        List<User> userList = UserFactory.createUserList(15);
        // WHEN
        List<UserH2Dto> result = mapper.mapToDtoList(userList);
        // THEN
        assertThat(result).hasSameSizeAs(userList);
        IntStream.range(0, result.size()).forEach(k -> assertMapUserToDtoIsOk(result.get(k), userList.get(k), objectMapper));
    }

    @Test
    void mapToModel_with_a_null_dto_should_return_null() {
        // WHEN
        User result = mapper.mapToModel(null);
        // THEN
        assertThat(result).isNull();
    }

    @Test
    void mapToModel_with_a_not_null_model_should_map_fields_correctly() {
        // GIVEN
        UserH2Dto dto = UserH2DtoFactory.createUserH2Dto("Blumenkranz");
        // WHEN
        User result = mapper.mapToModel(dto);
        // THEN
        assertMapUserToModelIsOk(result, dto, objectMapper);
    }

    static void assertMapUserToModelIsOk(User result, UserH2Dto dto, ObjectMapper objectMapper) {
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(dto.getId());
        assertThat(result.nom()).isEqualTo(dto.getNom());
        try {
            List<LocalDate> expected = objectMapper.readValue(dto.getDatesOccupees(), new TypeReference<>() {
            });
            result.datesOccupees().forEach(date -> assertThat(expected).containsAll(result.datesOccupees()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void mapToModelList_should_map_all_objects_correctly() {
        // GIVEN
        List<UserH2Dto> dtoList = UserH2DtoFactory.createUserH2DTOList(17);
        // WHEN
        List<User> result = mapper.mapToModelList(dtoList);
        // THEN
        assertThat(result).hasSameSizeAs(dtoList);
        IntStream.range(0, dtoList.size()).forEach(k -> assertMapUserToModelIsOk(result.get(k), dtoList.get(k), objectMapper));
    }
}
